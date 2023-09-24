package com.allane.vehicleleasingservice.customers;

import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public CustomerEntity mapFromDto(CustomerDto customerDto){

        CustomerEntity entity = new CustomerEntity();
        entity.setFirstName(customerDto.getFirstName());
        entity.setLastName(customerDto.getLastName());
        entity.setBirthDate(customerDto.getBirthDate());

        return entity;
    }

    public CustomerDto mapFromEntity(CustomerEntity customerEntity){

        return CustomerDto
                .builder()
                .id(customerEntity.getId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .birthDate(customerEntity.getBirthDate())
                .build();
    }
}
