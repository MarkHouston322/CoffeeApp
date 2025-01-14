package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.constants.EmployeeConstants;
import CoffeeApp.employeesservice.dto.ErrorResponseDto;
import CoffeeApp.employeesservice.dto.employeeDto.EmployeeDto;
import CoffeeApp.employeesservice.dto.employeeDto.EmployeeResponse;
import CoffeeApp.employeesservice.dto.ResponseDto;
import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Employees in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE employees"
)
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Fetch  all Employees  REST API",
            description = "REST API to fetch all Employees"
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
    public EmployeeResponse findAll() {
        return employeeService.findAll();
    }


    @Operation(
            summary = "Fetch Employee Details REST API",
            description = "REST API to fetch Employee details based on a name"
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
    @GetMapping("/get/{name}")
    public ResponseEntity<EmployeeDto> findByName(@PathVariable("name") String name) {
        EmployeeDto employeeDto = convertToEmployeeDto(employeeService.findByName(name));
        return ResponseEntity.status(HttpStatus.OK).body(employeeDto);
    }

    @Operation(
            summary = "Delete Employee REST API",
            description = "REST API to delete Employee based on a name"
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
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<ResponseDto> deleteEmployee(@PathVariable("name") String name) {
        employeeService.deleteEmployee(name);
        return responseStatusOk();
    }

    @Operation(
            summary = "Assign position REST API",
            description = "REST API to assign position to Employee based on a name"
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
    @PatchMapping("/assignPosition/{name}/{positionId}")
    public ResponseEntity<ResponseDto> assignRole(@PathVariable("name") String name,
                                                  @PathVariable("positionId") Integer positionId) {
        employeeService.assignPosition(name, positionId);
        return responseStatusOk();
    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(EmployeeConstants.STATUS_200, EmployeeConstants.MESSAGE_200));
    }

    private EmployeeDto convertToEmployeeDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

}
