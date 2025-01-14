package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.constants.SalaryConstants;
import CoffeeApp.employeesservice.dto.ErrorResponseDto;
import CoffeeApp.employeesservice.dto.ResponseDto;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryDto;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryResponse;
import CoffeeApp.employeesservice.services.SalaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Salaries in Coffee App",
        description = "CRUD REST APIs in Coffee App to UPDATE, FETCH AND DELETE salaries"
)
public class SalaryController {

    private final SalaryService salaryService;

    @Operation(
            summary = "Fetch  all Salaries  REST API",
            description = "REST API to fetch all Salaries"
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
    public SalaryResponse findAll() {
        return salaryService.findAll();
    }

    @Operation(
            summary = "Fetch Salary Details REST API",
            description = "REST API to fetch Salary details based on an id"
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
    public ResponseEntity<SalaryDto> findById(@PathVariable("id") Integer id) {
        SalaryDto salaryDto = salaryService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(salaryDto);
    }

    @Operation(
            summary = "Fetch Salary Details REST API",
            description = "REST API to fetch Salary details based on a employee id"
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
    @GetMapping("/employee/{name}")
    public SalaryResponse findByEmployeeName(@PathVariable("name") String name) {
        return salaryService.findByEmployeeName(name);
    }

    @Operation(
            summary = "Delete Salary REST API",
            description = "REST API to delete Salary based on an id"
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
    public ResponseEntity<ResponseDto> deleteSalary(@PathVariable("id") Integer id) {
        salaryService.deleteSalary(id);
        return responseStatusOk();

    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(SalaryConstants.STATUS_200, SalaryConstants.MESSAGE_200));
    }
}
