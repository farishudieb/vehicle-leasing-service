package com.allane.vehicleleasingservice.unit;

import com.allane.vehicleleasingservice.exceptions.NotFoundException;
import com.allane.vehicleleasingservice.customers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.allane.vehicleleasingservice.util.DummyObjects.createDummyCustomerDto;
import static com.allane.vehicleleasingservice.util.DummyObjects.createDummyCustomerEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private static CustomerService customerService;

    @BeforeEach
    public void init(){

        customerService = new CustomerService(customerRepository, new CustomerMapper());
    }

    @Test
    void whenCreatingNewCustomer_repositoryIsCalledOnceAndReturnedCustomerContainsAllFields(){

        CustomerEntity dummyCustomerEntity = createDummyCustomerEntity();
        CustomerDto dummyCustomerDto = createDummyCustomerDto();

        when(customerRepository.save(any())).thenReturn(dummyCustomerEntity);

        CustomerDto createdCustomer = customerService.createCustomer(dummyCustomerDto);

        verify(customerRepository, times(1)).save(any());
        assertEquals(createdCustomer.getFirstName(), dummyCustomerDto.getFirstName());
        assertEquals(createdCustomer.getLastName(), dummyCustomerDto.getLastName());
        assertEquals(createdCustomer.getBirthDate(), dummyCustomerDto.getBirthDate());
        assertNotNull(createdCustomer.getId());
    }

    void testUpdateExistingCustomer_repositoryIsCalledTwiceAndCustomerDataIsUpdated(){

        CustomerEntity dummyCustomerEntity = createDummyCustomerEntity();
        CustomerDto dummyCustomerDto = createDummyCustomerDto();

        when(customerRepository.findById(eq(1L))).thenReturn(Optional.of(dummyCustomerEntity));
        when(customerRepository.save(any())).thenReturn(dummyCustomerEntity);

        CustomerDto createdCustomer = customerService.updateCustomer(dummyCustomerDto);
        verify(customerRepository, times(2));//One for find and one for save
        assertEquals(createdCustomer.getFirstName(), dummyCustomerDto.getFirstName());
        assertEquals(createdCustomer.getLastName(), dummyCustomerDto.getLastName());
        assertEquals(createdCustomer.getBirthDate(), dummyCustomerDto.getBirthDate());
        assertNotNull(createdCustomer.getId());
    }

    void testUpdateNotExistingCustomer_repositoryIsCalledOnceAndExceptionIsThrown(){

        CustomerDto dummyCustomerDto = createDummyCustomerDto();

        when(customerRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(dummyCustomerDto));
        verify(customerRepository, times(1));
    }

    void testDeleteNotExistingCustomer_repositoryIsCalledOnceAndExceptionIsThrown(){

        when(customerRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.deleteCustomer(1L));
        verify(customerRepository, times(1));
    }
}
