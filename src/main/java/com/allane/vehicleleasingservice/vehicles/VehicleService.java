package com.allane.vehicleleasingservice.vehicles;

import com.allane.vehicleleasingservice.InvalidRequestException;
import com.allane.vehicleleasingservice.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository,
                          VehicleMapper vehicleMapper){

        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    public VehicleDto createVehicle(VehicleDto vehicleDto) {

        if(!ObjectUtils.isEmpty(vehicleDto.getVin())){
            validateVehicleVIN(null, vehicleDto.getVin());
        }

        VehicleEntity vehicleEntity = vehicleMapper.mapFromDto(vehicleDto);
        VehicleEntity saved = vehicleRepository.save(vehicleEntity);

        return vehicleMapper.mapFromEntity(saved);
    }

    public VehicleDto updateVehicle(VehicleDto newVehicleInfo) {

        Optional<VehicleEntity> optionalVehicleEntity = vehicleRepository.findById(newVehicleInfo.getId());
        if(optionalVehicleEntity.isEmpty())
            throw new NotFoundException("Could not find a vehicle with this id");

        VehicleEntity oldVehicleInfo = optionalVehicleEntity.get();
        if(hasVINChanged(oldVehicleInfo.getVin(), newVehicleInfo.getVin())){
            validateVehicleVIN(oldVehicleInfo.getVin(), newVehicleInfo.getVin());
        }

        oldVehicleInfo.setVin(newVehicleInfo.getVin());
        oldVehicleInfo.setPrice(newVehicleInfo.getPrice());
        oldVehicleInfo.setBrand(newVehicleInfo.getBrand());
        oldVehicleInfo.setModel(newVehicleInfo.getModel());
        oldVehicleInfo.setModelYear(newVehicleInfo.getModelYear());
        VehicleEntity saved = vehicleRepository.save(oldVehicleInfo);

        return vehicleMapper.mapFromEntity(saved);
    }

    public void deleteVehicle(Long vehicleId){

        vehicleRepository.delete(getVehicleEntity(vehicleId));
    }

    public VehicleDto getVehicle(Long vehicleId){

        return vehicleMapper.mapFromEntity(getVehicleEntity(vehicleId));
    }

    public VehicleEntity getVehicleEntity(Long id){

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(id);
        if(vehicleEntity.isEmpty())
            throw new NotFoundException("Could not find vehicle with this id");

        return vehicleEntity.get();
    }

    private boolean hasVINChanged(String oldVIN, String newVIN){
        oldVIN = oldVIN == null ? "" : oldVIN;//To prevent NPE while comparing values, as anyone could be null
        newVIN = newVIN == null ? "" : newVIN;

        return !oldVIN.equals(newVIN);
    }
    public void validateVehicleVIN(String oldVehicleVIN, String newVehicleVIN){

        //Prevent updating vin to null if it had already a value set
        //Initially this value is accepted to be null, but once set, it can only be changed to a new value, but not to be removed completely
        if(!ObjectUtils.isEmpty(oldVehicleVIN) && ObjectUtils.isEmpty(newVehicleVIN)){

            throw new InvalidRequestException("Is not allowed to empty the value of vin while it was set!");
        }

        Optional<VehicleEntity> vehicleEntityByVIN = vehicleRepository.findByVin(newVehicleVIN);
        //Here we make sure that the vehicle is not used by another leasing contract
        if(vehicleEntityByVIN.isPresent())
            throw new InvalidRequestException("Vehicle already used in another leasing contract");
    }
}
