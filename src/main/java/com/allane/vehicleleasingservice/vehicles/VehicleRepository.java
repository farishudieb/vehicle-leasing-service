package com.allane.vehicleleasingservice.vehicles;

import com.allane.vehicleleasingservice.customers.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    Optional<VehicleEntity> findByVin(String vin);
}
