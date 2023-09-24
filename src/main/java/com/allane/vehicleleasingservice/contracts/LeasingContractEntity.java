package com.allane.vehicleleasingservice.contracts;

import com.allane.vehicleleasingservice.customers.CustomerEntity;
import com.allane.vehicleleasingservice.vehicles.VehicleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "leasing_contracts")
@Getter
@Setter
@NoArgsConstructor
@EnableJpaAuditing
public class LeasingContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leasing_contract_id_sequence")
    @SequenceGenerator(name="leasing_contract_id_sequence", sequenceName = "leasing_contract_id_sequence")
    private Long id;

    @Column(name = "contract_number", nullable = false)
    private String contractNumber;

    @Column(name = "monthly_rate", nullable = false)
    private BigDecimal monthlyRate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customerEntity;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private VehicleEntity vehicleEntity;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeasingContractEntity that = (LeasingContractEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
