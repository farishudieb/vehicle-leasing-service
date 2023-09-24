package com.allane.vehicleleasingservice.vehicles;

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
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@EnableJpaAuditing
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_id_sequence")
    @SequenceGenerator(name="vehicle_id_sequence", sequenceName = "vehicle_id_sequence")
    private Long id;

    @Column(name="brand", nullable = false)
    private String brand;

    @Column(name="model", nullable = false)
    private String model;

    @Column(name="model_year", nullable = false)
    private Integer modelYear;

    @Column(name="vin")
    private String vin;

    @Column(name="price", nullable = false)
    private BigDecimal price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleEntity that = (VehicleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
