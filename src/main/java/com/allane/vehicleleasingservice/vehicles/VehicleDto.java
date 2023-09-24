package com.allane.vehicleleasingservice.vehicles;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Value
@JsonRootName("Vehicle")
public class VehicleDto {

    @Schema(description = "Id of the vehicle, is not required for POST requests", example = "1")
    Long id;

    @NotNull
    @Schema(description = "Brand of the vehicle", example = "BMW", requiredMode = Schema.RequiredMode.REQUIRED)
    String brand;

    @NotNull
    @Schema(description = "Model of the vehicle", example = "X5", requiredMode = Schema.RequiredMode.REQUIRED)
    String model;

    @NotNull
    @Schema(description = "Model year of the vehicle", example = "2023", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer modelYear;

    @Schema(description = "Vehicle Identification Number, this value initially is not required, but once set, it cannot be set empty again, but it can be changed", example = "1")
    String vin;

    @NotNull
    @Schema(description = "Price of the vehicle", example = "24565.99", requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal price;
}
