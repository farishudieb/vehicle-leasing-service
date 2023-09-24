package com.allane.vehicleleasingservice.contracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LeasingContractRepository extends JpaRepository<LeasingContractEntity, Long> {

    Optional<LeasingContractEntity> findByContractNumber(String contractNumber);
    Optional<LeasingContractEntity> findByVehicleEntity_Id(Long vehicleId);
}
