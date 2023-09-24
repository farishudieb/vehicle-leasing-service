package com.allane.vehicleleasingservice.api;

import com.allane.vehicleleasingservice.customers.CustomerDto;
import com.allane.vehicleleasingservice.customers.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customers")
public class CustomerApi {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Create new Customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer created, and response with the id is returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Required fields are not filled") })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> createNewCustomer(@RequestBody CustomerDto customerDto){

        CustomerDto savedCustomer = customerService.createCustomer(customerDto);

        return ResponseEntity.ok(savedCustomer);//We could use HATEOS to send the URI of the created resource, but this will required the FE to hit another GET request to get the created resource representation
    }

    @Operation(summary = "Get customer details by it's id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer is found and details are returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer with this id is not found") })
    @GetMapping(
            path = "/{customerId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable
                                                   @Parameter(description = "Customer id", example = "1", required = true)
                                                   Long customerId){

        CustomerDto customer = customerService.getCustomer(customerId);

        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Update customer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer details are updated and returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer with this id is not found") })
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto){

        CustomerDto savedCustomer = customerService.updateCustomer(customerDto);

        return ResponseEntity.ok(savedCustomer);
    }

    @Operation(summary = "Delete specific customer by id. If customer has contracts this operation will fail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer is found and details are returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Customer with this id is not found"),
            @ApiResponse(responseCode = "500", description = "If customer has contracts") })
    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable
                                               @Parameter(description = "Customer id to delete", example = "1", required = true)
                                               Long customerId){

        customerService.deleteCustomer(customerId);

        return ResponseEntity.accepted().build();
    }
}
