package com.allane.vehicleleasingservice.vehicles;

import org.springframework.stereotype.Service;

@Service
public class VehicleMapper {

    public VehicleEntity mapFromDto(VehicleDto vehicleDto){

        VehicleEntity entity = new VehicleEntity();
        entity.setId(vehicleDto.getId());
        entity.setVin(vehicleDto.getVin());
        entity.setBrand(vehicleDto.getBrand());
        entity.setModel(vehicleDto.getModel());
        entity.setModelYear(vehicleDto.getModelYear());
        entity.setPrice(vehicleDto.getPrice());

        return entity;
    }

    public VehicleDto mapFromEntity(VehicleEntity vehicleEntity){

        return VehicleDto
                .builder()
                .id(vehicleEntity.getId())
                .vin(vehicleEntity.getVin())
                .brand(vehicleEntity.getBrand())
                .model(vehicleEntity.getModel())
                .modelYear(vehicleEntity.getModelYear())
                .price(vehicleEntity.getPrice())
                .build();
    }
}
