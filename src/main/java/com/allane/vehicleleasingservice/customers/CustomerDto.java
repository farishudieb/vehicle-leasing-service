package com.allane.vehicleleasingservice.customers;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Value
@JsonRootName("Customer")

public class CustomerDto {

    @Schema(description = "Id of the customer, is not required for POST requests", example = "1")
    Long id;

    @NotNull
    @Schema(description = "Customer first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    String firstName;

    @NotNull
    @Schema(description = "Customer last name", example = "Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    String lastName;

    @NotNull
    @Schema( type = "string", description = "Customer birth date", example = "15.12.1975", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate birthDate;
}
