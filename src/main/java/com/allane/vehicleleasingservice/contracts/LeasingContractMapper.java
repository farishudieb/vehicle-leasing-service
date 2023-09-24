package com.allane.vehicleleasingservice.contracts;

import com.allane.vehicleleasingservice.customers.CustomerMapper;
import com.allane.vehicleleasingservice.vehicles.VehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeasingContractMapper {

    private final CustomerMapper customerMapper;

    private final VehicleMapper vehicleMapper;

    public LeasingContractMapper(CustomerMapper customerMapper,
                                 VehicleMapper vehicleMapper) {

        this.customerMapper = customerMapper;
        this.vehicleMapper = vehicleMapper;
    }
    public LeasingContractEntity mapFromDto(LeasingContractDto leasingContractDto){

        LeasingContractEntity entity = new LeasingContractEntity();
        entity.setContractNumber(leasingContractDto.getContractNumber());
        entity.setId(leasingContractDto.getId());
        entity.setMonthlyRate(leasingContractDto.getMonthlyRate());

        return entity;
    }

    public LeasingContractDto mapFromEntity(LeasingContractEntity leasingContractEntity){

        return LeasingContractDto
                .builder()
                .contractNumber(leasingContractEntity.getContractNumber())
                .id(leasingContractEntity.getId())
                .monthlyRate(leasingContractEntity.getMonthlyRate())
                .customerId(leasingContractEntity.getCustomerEntity().getId())
                .vehicleId(leasingContractEntity.getVehicleEntity().getId())
                .build();
    }

    public LeasingContractOverviewDto mapFromEntityToOverviewDto(LeasingContractEntity leasingContractEntity){

        return LeasingContractOverviewDto
                .builder()
                .contractNumber(leasingContractEntity.getContractNumber())
                .id(leasingContractEntity.getId())
                .monthlyRate(leasingContractEntity.getMonthlyRate())
                .customer(customerMapper.mapFromEntity(leasingContractEntity.getCustomerEntity()))
                .vehicle(vehicleMapper.mapFromEntity(leasingContractEntity.getVehicleEntity()))
                .build();
    }
}
