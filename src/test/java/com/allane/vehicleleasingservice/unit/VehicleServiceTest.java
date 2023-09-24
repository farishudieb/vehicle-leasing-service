package com.allane.vehicleleasingservice.unit;

import com.allane.vehicleleasingservice.InvalidRequestException;
import com.allane.vehicleleasingservice.NotFoundException;
import com.allane.vehicleleasingservice.vehicles.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.allane.vehicleleasingservice.util.DummyObjects.createDummyVehicleDto;
import static com.allane.vehicleleasingservice.util.DummyObjects.createDummyVehicleEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private static VehicleService vehicleService;

    @BeforeEach
    public void init(){

        vehicleService = new VehicleService(vehicleRepository, new VehicleMapper());
    }

    @Test
    void whenCreatingNewVehicle_repositoryIsCalledTwiceAndReturnedVehicleContainsAllFields(){

        VehicleEntity dummyVehicleEntity = createDummyVehicleEntity();
        VehicleDto dummyVehicleDto = createDummyVehicleDto(false);

        when(vehicleRepository.save(any())).thenReturn(dummyVehicleEntity);

        VehicleDto createdVehicle = vehicleService.createVehicle(dummyVehicleDto);

        verify(vehicleRepository, times(1)).save(any());
        verify(vehicleRepository, times(1)).findByVin(eq(dummyVehicleDto.getVin()));

        assertEquals(createdVehicle.getVin(), dummyVehicleDto.getVin());
        assertEquals(createdVehicle.getBrand(), dummyVehicleDto.getBrand());
        assertEquals(createdVehicle.getPrice(), dummyVehicleDto.getPrice());
        assertEquals(createdVehicle.getModel(), dummyVehicleDto.getModel());
        assertEquals(createdVehicle.getModelYear(), dummyVehicleDto.getModelYear());
        assertNotNull(createdVehicle.getId());
    }

    @Test
    void whenUpdatingExistingVehicleDataAndSetNullValueInVINField_exceptionWillBeThrown(){

        VehicleEntity dummyVehicleEntity = createDummyVehicleEntity();
        VehicleDto dummyVehicleDto = createDummyVehicleDto(true);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(dummyVehicleEntity));

        assertThrows(InvalidRequestException.class, () -> vehicleService.updateVehicle(dummyVehicleDto));
    }

    @Test
    void testUpdateNotExistingVehicle_repositoryIsCalledOnceAndExceptionIsThrown(){

        VehicleDto dummyVehicleDto = createDummyVehicleDto(false);

        when(vehicleRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.updateVehicle(dummyVehicleDto));
        verify(vehicleRepository, times(1)).findById(eq(1L));
    }

    @Test
    void validateVehicleVIN_shouldThrowException_WhenVINAlreadyExists(){

        VehicleEntity dummyVehicleEntity = createDummyVehicleEntity();
        String oldVIN = "1234";
        String newVIN = dummyVehicleEntity.getVin();

        when(vehicleRepository.findByVin(eq(newVIN))).thenReturn(Optional.of(dummyVehicleEntity));

        assertThrows(InvalidRequestException.class, () -> vehicleService.validateVehicleVIN(oldVIN, newVIN));
    }
}
