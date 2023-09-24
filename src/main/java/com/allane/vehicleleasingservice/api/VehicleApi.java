package com.allane.vehicleleasingservice.api;

import com.allane.vehicleleasingservice.vehicles.VehicleDto;
import com.allane.vehicleleasingservice.vehicles.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("vehicles")
public class VehicleApi {

    @Autowired
    private VehicleService vehicleService;

    @Operation(summary = "Create new Vehicle.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle created, and response with the id is returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehicleDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Required fields are not filled, or vehicle VIN is not unique") })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDto> createNewVehicle(@RequestBody VehicleDto vehicleDto){

        VehicleDto vehicle = vehicleService.createVehicle(vehicleDto);

        return ResponseEntity.ok(vehicle);//We could use HATEOS to send the URI of the created resource, but this will required the FE to hit another GET request to get the created resource representation
    }

    @Operation(summary = "Get vehicle details by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle is found and details are returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehicleDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vehicle with this id is not found") })
    @GetMapping(
            path = "/{vehicleId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDto> getVehicle(@PathVariable
                                                 @Parameter(description = "Vehicle id", example = "1", required = true)
                                                 Long vehicleId){

        VehicleDto vehicle = vehicleService.getVehicle(vehicleId);

        return ResponseEntity.ok(vehicle);
    }

    @Operation(summary = "Update vehicle details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle details are updated and returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehicleDto.class)) }),
            @ApiResponse(responseCode = "400", description = "VIN field updated value is null"),
            @ApiResponse(responseCode = "404", description = "Vehicle with this id is not found") })
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleDto> updateVehicle(@RequestBody VehicleDto vehicleDto){

        VehicleDto vehicle = vehicleService.updateVehicle(vehicleDto);

        return ResponseEntity.ok(vehicle);
    }

    @Operation(summary = "Delete specific vehicle by id. If the vehicle has contracts this operation will fail")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle is found and details are returned",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehicleDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Vehicle with this id is not found"),
            @ApiResponse(responseCode = "500", description = "If the vehicle is assigned to a contract") })
    @DeleteMapping(path = "/{vehicleId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable
                                               @Parameter(description = "Vehicle id", example = "1", required = true)
                                               Long vehicleId){

        vehicleService.deleteVehicle(vehicleId);

        return ResponseEntity.accepted().build();
    }
}
