package CoffeeApp.customerservice.controllers;

import CoffeeApp.customerservice.constants.CustomerConstants;
import CoffeeApp.customerservice.dto.ErrorResponseDto;
import CoffeeApp.customerservice.dto.customerDto.AddCustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerResponse;
import CoffeeApp.customerservice.dto.ResponseDto;
import CoffeeApp.customerservice.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Customers in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE customer details"
)
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Fetch  all Customers REST API",
            description = "REST API to fetch all Customers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping
    public CustomerResponse findAll() {
        return customerService.findAll();
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Integer id) {
        CustomerDto customer = customerService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on a mobile number or second name"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/get/{query}")
    public Map<String, CustomerResponse> getCustomer(@PathVariable("query") String query) {
        Map<String, CustomerResponse> users = new HashMap<>();
        CustomerResponse usersBySecondName = customerService.findBySecondName(query);
        CustomerResponse usersByMobileNumber = customerService.findByMobileNumber(query);
        users.put("Users find by second name", usersBySecondName);
        users.put("Users find by mobile number", usersByMobileNumber);
        return users;
    }

    @Operation(
            summary = "Add Customer REST API",
            description = "REST API to add new Customer inside Coffee App"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addCustomer(@Valid @RequestBody AddCustomerDto addCustomerDto) {
        customerService.addCustomer(addCustomerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Customer Details REST API",
            description = "REST API to update Customer details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseDto> updateCustomer(@PathVariable("id") Integer id,
                                                      @Valid @RequestBody AddCustomerDto addCustomerDto) {
        customerService.updateCustomer(id, addCustomerDto);
        return responseStatusOk();

    }

    @Operation(
            summary = "Delete Customer Details REST API",
            description = "REST API to delete Customer details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
        return responseStatusOk();
    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }
}
