package com.allane.vehicleleasingservice.api;

import com.allane.vehicleleasingservice.contracts.LeasingContractDto;
import com.allane.vehicleleasingservice.contracts.LeasingContractOverviewDto;
import com.allane.vehicleleasingservice.contracts.LeasingContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("leasing-contracts")
public class LeasingContractApi {

    @Autowired
    private LeasingContractService leasingContractService;

    @Operation(summary = "Create new leasing contract. Customer, and Vehicle should be already created ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leasing contract created, and response with the id is returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeasingContractDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Required fields are not filled, or if the contract number provided is already used in another contract"),
            @ApiResponse(responseCode = "404", description = "If the customer or vehicle ids are not found") })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeasingContractDto> createNewLeasingContract(@RequestBody @Validated LeasingContractDto leasingContractDto){

        LeasingContractDto leasingContract = leasingContractService.createLeasingContract(leasingContractDto);

        return ResponseEntity.ok(leasingContract);//We could use HATEOAS to send the URI of the created resource, but this will required the FE to hit another GET request to get the created resource representation
    }

    @Operation(summary = "Return specific leasing contract by it's id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leasing contract with this id is found, and it's details returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeasingContractDto.class)) }),
            @ApiResponse(responseCode = "404", description = "If no leasing contract with this id is found") })
    @GetMapping(
            path = "/{leasingContractId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeasingContractDto> getLeasingContract(@PathVariable
                                                                 @Parameter(description = "Id of the contract we want to find it", example = "1252", required = true)
                                                                 Long leasingContractId){

        LeasingContractDto leasingContract = leasingContractService.getLeasingContract(leasingContractId);

        return ResponseEntity.ok(leasingContract);
    }

    @Operation(summary = "Return a list of the stored leasing contracts that belongs to a specific request page, data is sorted initially by created date desc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data found and returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeasingContractOverviewDto.class)) }) })
    @GetMapping(
            path = "overview/{page}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LeasingContractOverviewDto>> getLeasingContractOverviewList(@PathVariable
                                                                                           @Parameter(description = "Request page, starts with page 0", example = "0", required = true)
                                                                                           @NotNull Integer page){

        List<LeasingContractOverviewDto> leasingContractsOverviewPage = leasingContractService.getLeasingContractsPage(page);

        return ResponseEntity.ok(leasingContractsOverviewPage);
    }

    @Operation(summary = "Update an existing leasing contract details. Users are allowed to update contract data, or assign a new vehicle or a new customer to this contract. Old vehicle or customers will not be deleted automatically, rather is should be done manually")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leasing contract updated, and saved data are returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeasingContractDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Required fields are not filled, if the contract number provided is already used in another contract, or selected vehicle is used by another contract"),
            @ApiResponse(responseCode = "404", description = "If the customer or vehicle ids are not found") })
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LeasingContractDto> updateLeasingContract(@RequestBody LeasingContractDto leasingContractDto){

        LeasingContractDto updated = leasingContractService.updateLeasingContract(leasingContractDto);

        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletes an already existing contract. Deletion of a contract will automatically delete its assigned vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Leasing contract is deleted along with the vehicle"),
            @ApiResponse(responseCode = "404", description = "If the contract is not found") })
    @DeleteMapping(path = "/{leasingContractId}")
    public ResponseEntity<Void> deleteLeasingContract(@PathVariable
                                                      @Parameter(description = "Id of the contract we want to delete", example = "1252", required = true)
                                                      @NotNull Long leasingContractId){

        leasingContractService.deleteLeasingContract(leasingContractId);

        return ResponseEntity.accepted().build();
    }
}
