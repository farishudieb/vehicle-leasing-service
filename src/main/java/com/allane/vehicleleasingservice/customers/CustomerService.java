package com.allane.vehicleleasingservice.customers;

import com.allane.vehicleleasingservice.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository,
                           CustomerMapper customerMapper){

        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerDto createCustomer(CustomerDto newCustomer){

        CustomerEntity customerEntity = customerMapper.mapFromDto(newCustomer);
        CustomerEntity saved = customerRepository.save(customerEntity);

        return customerMapper.mapFromEntity(saved);
    }

    public CustomerDto updateCustomer(CustomerDto newCustomerDetails){

        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(newCustomerDetails.getId());
        if(optionalCustomerEntity.isEmpty())
            throw new NotFoundException("No customer with this id exists");

        CustomerEntity customerEntity = optionalCustomerEntity.get();
        customerEntity.setBirthDate(newCustomerDetails.getBirthDate());
        customerEntity.setFirstName(newCustomerDetails.getFirstName());
        customerEntity.setLastName(newCustomerDetails.getLastName());

        CustomerEntity saved = customerRepository.save(customerEntity);

        return customerMapper.mapFromEntity(saved);
    }

    public void deleteCustomer(Long id){

        CustomerEntity customerEntity = getCustomerEntity(id);
        customerRepository.delete(customerEntity);
    }

    public CustomerDto getCustomer(Long id){

        return customerMapper.mapFromEntity(getCustomerEntity(id));

    }

    public CustomerEntity getCustomerEntity(Long id){

        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findById(id);
        if(optionalCustomerEntity.isEmpty())
            throw new NotFoundException("No customer with this id exists");

        return optionalCustomerEntity.get();
    }
}
