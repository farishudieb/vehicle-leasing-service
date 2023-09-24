package com.allane.vehicleleasingservice.integration;

import com.allane.vehicleleasingservice.util.DummyObjects;
import com.allane.vehicleleasingservice.util.JSONUtil;
import com.allane.vehicleleasingservice.exceptions.NotFoundException;
import com.allane.vehicleleasingservice.contracts.LeasingContractDto;
import com.allane.vehicleleasingservice.contracts.LeasingContractOverviewDto;
import com.allane.vehicleleasingservice.contracts.LeasingContractRepository;
import com.allane.vehicleleasingservice.contracts.LeasingContractService;
import com.allane.vehicleleasingservice.customers.CustomerDto;
import com.allane.vehicleleasingservice.customers.CustomerRepository;
import com.allane.vehicleleasingservice.customers.CustomerService;
import com.allane.vehicleleasingservice.vehicles.VehicleDto;
import com.allane.vehicleleasingservice.vehicles.VehicleRepository;
import com.allane.vehicleleasingservice.vehicles.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.allane.vehicleleasingservice.contracts.LeasingContractService.PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LeasingContractApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private LeasingContractService leasingContractService;

    @Autowired
    private LeasingContractRepository leasingContractRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    VehicleRepository vehicleRepository;

    @BeforeEach
    public void clearDB(){

        leasingContractRepository.deleteAll();
        customerRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    void createNewLeasingContract_shouldReturnOkWithValidResponse() throws Exception {

        CustomerDto savedCustomer = createDummyCustomer();
        VehicleDto savedVehicle = createDummyVehicle();
        LeasingContractDto leasingContractDto = DummyObjects.createDummyLeasingContractDto(savedCustomer.getId(), savedVehicle.getId());
        String createLeasingContractRequest = JSONUtil.objectToJsonString(leasingContractDto);

        String response = mockMvc.perform(MockMvcRequestBuilders
                .post("/leasing-contracts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(createLeasingContractRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LeasingContractDto createdLeasingContract = JSONUtil.JsonStringToObject(LeasingContractDto.class, response);

        assertNotNull(createdLeasingContract.getId());
        assertEquals(leasingContractDto.getContractNumber(), createdLeasingContract.getContractNumber());
        assertEquals(leasingContractDto.getMonthlyRate(), createdLeasingContract.getMonthlyRate());
        assertEquals(leasingContractDto.getCustomerId(), createdLeasingContract.getCustomerId());
        assertEquals(leasingContractDto.getVehicleId(), createdLeasingContract.getVehicleId());
    }

    @Test
    void createNewLeasingContract_shouldReturnBadRequest_whenRequiredFieldIsNull() throws Exception {

        CustomerDto savedCustomer = createDummyCustomer();
        VehicleDto savedVehicle = createDummyVehicle();
        LeasingContractDto leasingContractDto = DummyObjects.createDummyLeasingContractDtoWithContractNumberNull(savedCustomer.getId(), savedVehicle.getId());

        String createLeasingContractRequest = JSONUtil.objectToJsonString(leasingContractDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(createLeasingContractRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewLeasingContract_shouldReturnBadRequest_whenContractNumberIsAlreadyUsed() throws Exception {

        VehicleDto savedVehicle = createDummyVehicle();
        CustomerDto savedCustomer = createDummyCustomer();
        LeasingContractDto leasingContractDto = DummyObjects.createDummyLeasingContractDto(savedCustomer.getId(), savedVehicle.getId());

        String createLeasingContractRequest = JSONUtil.objectToJsonString(leasingContractDto);
        //first call the request is valid
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(createLeasingContractRequest))
                .andExpect(status().isOk());

        //We resend same request, which contains same contractNumber, and this should fail
        VehicleDto anotherVehicle = createDummyVehicle();
        LeasingContractDto secondLeasingContractWithDifferentVehicle = DummyObjects.createDummyLeasingContractDto(savedCustomer.getId(), anotherVehicle.getId());

        String secondCreateLeasingContractRequest = JSONUtil.objectToJsonString(secondLeasingContractWithDifferentVehicle);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(secondCreateLeasingContractRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewLeasingContract_shouldReturnBadRequest_whenVehicleIsAlreadyUsedInAnotherContract() throws Exception {

        VehicleDto savedVehicle = createDummyVehicle();
        CustomerDto savedCustomer = createDummyCustomer();
        LeasingContractDto leasingContractDto = DummyObjects.createDummyLeasingContractDto(savedCustomer.getId(), savedVehicle.getId());

        String createLeasingContractRequest = JSONUtil.objectToJsonString(leasingContractDto);
        //first call the request is valid
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(createLeasingContractRequest))
                .andExpect(status().isOk());

        //We resend same request, which contains same contractNumber, and this should fail
        LeasingContractDto sameVehicleDifferentContractNumber = DummyObjects.createDummyLeasingContractDto(savedCustomer.getId(), savedVehicle.getId(), "ABC");

        String secondCreateLeasingContractRequest = JSONUtil.objectToJsonString(sameVehicleDifferentContractNumber);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(secondCreateLeasingContractRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNewLeasingContract_shouldReturnNotFound_whenCustomerIdIsNotFound() throws Exception {

        VehicleDto savedVehicle = createDummyVehicle();
        LeasingContractDto leasingContractDto = DummyObjects.createDummyLeasingContractDto(1L, savedVehicle.getId());

        String createLeasingContractRequest = JSONUtil.objectToJsonString(leasingContractDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(createLeasingContractRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void getExistingLeasingLeasingContractById_shouldReturnOkAndValidContractDetails() throws Exception {

        VehicleDto savedVehicle = createDummyVehicle();
        CustomerDto savedCustomer = createDummyCustomer();
        LeasingContractDto savedLeasingContract = createDummyLeasingContract(savedCustomer.getId(), savedVehicle.getId());

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/leasing-contracts/{leasingContractId}", savedLeasingContract.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LeasingContractDto retrievedContract = JSONUtil.JsonStringToObject(LeasingContractDto.class, response);
        assertEquals(savedLeasingContract.getId(), retrievedContract.getId());
        assertEquals(savedLeasingContract.getVehicleId(), retrievedContract.getVehicleId());
        assertEquals(savedLeasingContract.getContractNumber(), retrievedContract.getContractNumber());
        assertEquals(savedLeasingContract.getCustomerId(), retrievedContract.getCustomerId());
        assertEquals(savedLeasingContract.getMonthlyRate(), retrievedContract.getMonthlyRate());
    }

    @Test
    void getNotExistingLeasingLeasingContractById_shouldReturnNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/leasing-contracts/{leasingContractId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateExistingLeasingContract_whenAssigningNewVehicle_shouldReturnOkWithTheChanges() throws Exception {

        VehicleDto oldVehicle = createDummyVehicle();
        VehicleDto newVehicle = createDummyVehicle();
        CustomerDto savedCustomer = createDummyCustomer();
        LeasingContractDto savedLeasingContract = createDummyLeasingContract(savedCustomer.getId(), oldVehicle.getId());

        //here we update the above created contract and assign a new vehicle id
        LeasingContractDto leasingContractWithNewVehicleAssigned = DummyObjects.createDummyLeasingContractDto(savedCustomer.getId(), newVehicle.getId(), savedLeasingContract.getContractNumber(), savedLeasingContract.getId());

        String updateRequest = JSONUtil.objectToJsonString(leasingContractWithNewVehicleAssigned);

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .put("/leasing-contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LeasingContractDto updatedContract = JSONUtil.JsonStringToObject(LeasingContractDto.class, response);
        assertEquals(newVehicle.getId(), updatedContract.getVehicleId());
    }

    @Test
    void deleteExistingLeasingContract_shouldReturnAcceptedAndDeleteTheVehicle() throws Exception {

        VehicleDto savedVehicle = createDummyVehicle();
        CustomerDto savedCustomer = createDummyCustomer();
        LeasingContractDto savedLeasingContract = createDummyLeasingContract(savedCustomer.getId(), savedVehicle.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/leasing-contracts/{leasingContractId}", savedLeasingContract.getId()))
                .andExpect(status().isAccepted());
        assertThrows(NotFoundException.class, () -> vehicleService.getVehicle(savedVehicle.getId()));
        assertThrows(NotFoundException.class, () -> leasingContractService.getLeasingContract(savedLeasingContract.getId()));
    }

    @Test
    void getContractsOverviewList_shouldReturnOKAndAListWithContractAndCustomerAndVehicleDetails() throws Exception {

        CustomerDto savedCustomer = createDummyCustomer();
        List<Long> createdVehicleIds = createVehiclesAndGetIds(10);
        Map<Long, LeasingContractDto> leasingContracts = createLeasingContracts(savedCustomer.getId(), createdVehicleIds);

        String response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/leasing-contracts/overview/{page}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<LeasingContractOverviewDto> result = JSONUtil.JsonStringToObjectList(LeasingContractOverviewDto.class, response);
        assertEquals(PAGE_SIZE, result.size());
    }

    private Map<Long, LeasingContractDto> createLeasingContracts(Long customerId, List<Long> vehicleIds){

        return vehicleIds
                .stream()
                .map(vehicleId -> DummyObjects.createDummyLeasingContractDto(customerId, vehicleId, vehicleId+""))
                .map(leasingContractService::createLeasingContract)
                .collect(Collectors.toMap(LeasingContractDto::getId, leasingContractDto -> leasingContractDto));
    }

    private List<Long> createVehiclesAndGetIds(int count){

        return IntStream
                .range(0, count).mapToObj( i -> createDummyVehicle())
                .map(VehicleDto::getId)
                .collect(Collectors.toList());
    }

    private CustomerDto createDummyCustomer(){

        CustomerDto dummyCustomerDto = DummyObjects.createDummyCustomerDto();

        return customerService.createCustomer(dummyCustomerDto);
    }

    private VehicleDto createDummyVehicle(){

        VehicleDto dummyVehicleDto = DummyObjects.createDummyVehicleDto(true);

        return vehicleService.createVehicle(dummyVehicleDto);
    }

    private LeasingContractDto createDummyLeasingContract(Long customerId, Long vehicleId){

        LeasingContractDto dummyLeasingContractDto = DummyObjects.createDummyLeasingContractDto(customerId, vehicleId);
        return leasingContractService.createLeasingContract(dummyLeasingContractDto);
    }
}
