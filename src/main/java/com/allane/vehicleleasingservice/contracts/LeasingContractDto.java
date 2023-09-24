package com.allane.vehicleleasingservice.contracts;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Value
@JsonRootName("LeasingContract")
public class LeasingContractDto {

    @Schema(description = "Id of the contract, is not required in POST requests", example = "1")
    Long id;

    @NotNull
    @Schema(description = "Unique value per contract provided by the user", example = "8855DFC996600", requiredMode = Schema.RequiredMode.REQUIRED)
    String contractNumber;

    @NotNull
    @Schema(description = "Monthly leasing value", example = "232015.99", requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal monthlyRate;

    @NotNull
    @Schema(description = "Customer id of this contract", example = "1521", requiredMode = Schema.RequiredMode.REQUIRED)
    Long customerId;

    @NotNull
    @Schema(description = "Id of the leased vehicle", example = "5625", requiredMode = Schema.RequiredMode.REQUIRED)
    Long vehicleId;
}
