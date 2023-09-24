package com.allane.vehicleleasingservice.util;

import com.allane.vehicleleasingservice.contracts.LeasingContractDto;
import com.allane.vehicleleasingservice.customers.CustomerDto;
import com.allane.vehicleleasingservice.customers.CustomerEntity;
import com.allane.vehicleleasingservice.vehicles.VehicleDto;
import com.allane.vehicleleasingservice.vehicles.VehicleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DummyObjects {

    public static CustomerEntity createDummyCustomerEntity(){

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName("John");
        customerEntity.setLastName("Smith");
        customerEntity.setBirthDate(LocalDate.of(1975, 5, 5));
        customerEntity.setId(1L);

        return customerEntity;
    }

    public static CustomerDto createDummyCustomerDto(){

        return CustomerDto
                .builder()
                .firstName("John")
                .lastName("Smith")
                .birthDate(LocalDate.of(1975, 5, 5))
                .build();
    }

    public static VehicleEntity createDummyVehicleEntity(){

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setModel("X5");
        vehicleEntity.setModelYear(2022);
        vehicleEntity.setBrand("BMW");
        vehicleEntity.setVin("123456");
        vehicleEntity.setPrice(BigDecimal.valueOf(12354.55));
        vehicleEntity.setId(1L);

        return vehicleEntity;
    }

    public static VehicleDto createDummyVehicleDto(boolean vinFieldNull){

        return VehicleDto
                .builder()
                .model("X5")
                .modelYear(2022)
                .brand("BMW")
                .vin(vinFieldNull ? null : "123456")
                .price(BigDecimal.valueOf(12354.55))
                .id(1L)
                .build();
    }

    public static LeasingContractDto createDummyLeasingContractDto(Long customerId, Long vehicleId){

        return LeasingContractDto
                .builder()
                .customerId(customerId)
                .vehicleId(vehicleId)
                .monthlyRate(BigDecimal.valueOf(2500.63))
                .contractNumber("123456")
                .build();
    }

    public static LeasingContractDto createDummyLeasingContractDto(Long customerId, Long vehicleId, String contractNumber){

        return LeasingContractDto
                .builder()
                .customerId(customerId)
                .vehicleId(vehicleId)
                .monthlyRate(BigDecimal.valueOf(2500.63))
                .contractNumber(contractNumber)
                .build();
    }

    public static LeasingContractDto createDummyLeasingContractDto(Long customerId, Long vehicleId, String contractNumber, Long id){

        return LeasingContractDto
                .builder()
                .id(id)
                .customerId(customerId)
                .vehicleId(vehicleId)
                .monthlyRate(BigDecimal.valueOf(2500.63))
                .contractNumber(contractNumber)
                .build();
    }

    public static LeasingContractDto createDummyLeasingContractDtoWithContractNumberNull(Long customerId, Long vehicleId){

        return LeasingContractDto
                .builder()
                .customerId(customerId)
                .vehicleId(vehicleId)
                .monthlyRate(BigDecimal.valueOf(2500.63))
                .build();
    }
}
