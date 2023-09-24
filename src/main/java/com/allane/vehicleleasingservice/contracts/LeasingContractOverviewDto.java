package com.allane.vehicleleasingservice.contracts;

import com.allane.vehicleleasingservice.customers.CustomerDto;
import com.allane.vehicleleasingservice.vehicles.VehicleDto;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Value
@JsonRootName("LeasingContractOverview")
public class LeasingContractOverviewDto {

    @NotNull
    @Parameter(description = "Id of the contract, this is expected to be filled always", example = "1", required = true)
    long id;

    @NotNull
    @Parameter(description = "Unique value per contract provided by the user", example = "8855DFC996600",required = true)
    String contractNumber;

    @NotNull
    @Parameter(description = "Monthly leasing value", example = "232015.99",required = true)
    BigDecimal monthlyRate;

    @NotNull
    @Parameter(description = "Customer details of this contract", example = "1521",required = true)
    CustomerDto customer;

    @NotNull
    @Parameter(description = "Vehicle details of this contract", example = "5625",required = true)
    VehicleDto vehicle;

}
