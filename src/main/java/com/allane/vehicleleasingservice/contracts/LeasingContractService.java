package com.allane.vehicleleasingservice.contracts;

import com.allane.vehicleleasingservice.exceptions.InvalidRequestException;
import com.allane.vehicleleasingservice.exceptions.NotFoundException;
import com.allane.vehicleleasingservice.customers.CustomerEntity;
import com.allane.vehicleleasingservice.customers.CustomerService;
import com.allane.vehicleleasingservice.vehicles.VehicleEntity;
import com.allane.vehicleleasingservice.vehicles.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeasingContractService {

    private final LeasingContractRepository leasingContractRepository;
    private final LeasingContractMapper leasingContractMapper;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    public static final int PAGE_SIZE = 5;

    @Autowired
    public LeasingContractService(LeasingContractRepository leasingContractRepository,
                                  LeasingContractMapper leasingContractMapper,
                                  CustomerService customerService,
                                  VehicleService vehicleService){

        this.leasingContractRepository = leasingContractRepository;
        this.leasingContractMapper = leasingContractMapper;
        this.customerService = customerService;
        this.vehicleService = vehicleService;
    }

    public LeasingContractDto createLeasingContract(LeasingContractDto leasingContractDto){

        CustomerEntity customerEntity = customerService.getCustomerEntity(leasingContractDto.getCustomerId());
        VehicleEntity vehicleEntity = vehicleService.getVehicleEntity(leasingContractDto.getVehicleId());
        validateContractNumberIsUnique(leasingContractDto.getContractNumber());
        validateVehicleIsNotUsedInAnotherContract(leasingContractDto.getVehicleId());

        LeasingContractEntity leasingContractEntity = leasingContractMapper.mapFromDto(leasingContractDto);
        leasingContractEntity.setVehicleEntity(vehicleEntity);
        leasingContractEntity.setCustomerEntity(customerEntity);

        LeasingContractEntity saved = leasingContractRepository.save(leasingContractEntity);

        return leasingContractMapper.mapFromEntity(saved);
    }

    public LeasingContractDto updateLeasingContract(LeasingContractDto newLeasingContractInfo){

        LeasingContractEntity leasingContractEntity = getLeasingContractEntity(newLeasingContractInfo.getId());
        if(!leasingContractEntity.getContractNumber().equals(newLeasingContractInfo.getContractNumber()))
            validateContractNumberIsUnique(newLeasingContractInfo.getContractNumber());

        leasingContractEntity.setMonthlyRate(newLeasingContractInfo.getMonthlyRate());
        leasingContractEntity.setContractNumber(newLeasingContractInfo.getContractNumber());

        updateCustomerChanges(leasingContractEntity, newLeasingContractInfo);
        updateVehicleChanges(leasingContractEntity, newLeasingContractInfo);

        LeasingContractEntity saved = leasingContractRepository.save(leasingContractEntity);

        return leasingContractMapper.mapFromEntity(saved);
    }

    public void deleteLeasingContract(Long leasingContractId){

        LeasingContractEntity leasingContractEntity = getLeasingContractEntity(leasingContractId);
        Long vehicleId = leasingContractEntity.getVehicleEntity().getId();

        //Remove the relation, so deletion can be possible, we can use cascade on deletion, but for this the 1-to-1 mapping must be defined on both entities
        leasingContractEntity.setVehicleEntity(null);
        leasingContractRepository.delete(leasingContractEntity);
        //We remove vehicle as it has one-to-one relation with the contract, so whenever we delete a contract we delete its vehicle
        vehicleService.deleteVehicle(vehicleId);
    }

    public LeasingContractDto getLeasingContract(Long id){

        LeasingContractEntity leasingContractEntity = getLeasingContractEntity(id);

        return leasingContractMapper.mapFromEntity(leasingContractEntity);
    }
    public List<LeasingContractOverviewDto> getLeasingContractsPage(Integer page){

        //Get last created contracts first
        Sort createdAtSort = Sort.by(Sort.Order.desc("createdAt"));
        PageRequest pageRequest = PageRequest.of(page, PAGE_SIZE, createdAtSort);

        return leasingContractRepository.findAll(pageRequest)
                .stream().map(leasingContractMapper::mapFromEntityToOverviewDto)
                .collect(Collectors.toList());
    }

    private void validateContractNumberIsUnique(String contractNumber){

        Optional<LeasingContractEntity> byContractNumber = leasingContractRepository.findByContractNumber(contractNumber);
        byContractNumber.ifPresent(leasingContractEntity -> {
            throw new InvalidRequestException("Leasing contract with this number already exists");
        });
    }

    private void validateVehicleIsNotUsedInAnotherContract(Long vehicleId){

        Optional<LeasingContractEntity> byVehicleEntityId = leasingContractRepository.findByVehicleEntity_Id(vehicleId);
        if(byVehicleEntityId.isPresent())
            throw new InvalidRequestException("Vehicle is already used in another contract!");

    }

    private LeasingContractEntity getLeasingContractEntity(Long id){

        Optional<LeasingContractEntity> optionalLeasingContract = leasingContractRepository.findById(id);
        if(optionalLeasingContract.isEmpty())
            throw new NotFoundException("Could not find leasing contract with this id");

        return optionalLeasingContract.get();
    }

    private void updateVehicleChanges(LeasingContractEntity currentContract, LeasingContractDto updatedContract){

        if(!Objects.equals(currentContract.getVehicleEntity().getId(), updatedContract.getVehicleId())){

            VehicleEntity newVehicle = vehicleService.getVehicleEntity(updatedContract.getVehicleId());
            currentContract.setVehicleEntity(newVehicle);
        }
    }

    private void updateCustomerChanges(LeasingContractEntity currentContract, LeasingContractDto updatedContract){

        if(!Objects.equals(currentContract.getCustomerEntity().getId(), updatedContract.getCustomerId())){

            CustomerEntity newCustomer = customerService.getCustomerEntity(updatedContract.getCustomerId());
            currentContract.setCustomerEntity(newCustomer);
        }
    }
}
