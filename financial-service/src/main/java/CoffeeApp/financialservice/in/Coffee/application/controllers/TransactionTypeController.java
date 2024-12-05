package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.constants.TransactionTypeConstants;
import CoffeeApp.financialservice.in.Coffee.application.dto.ErrorResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.ResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.TransactionTypeService;
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

@RestController
@RequestMapping("/transactionTypes")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for transaction types in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE transaction types"
)
public class TransactionTypeController {

    private final TransactionTypeService transactionTypeService;

    @Operation(
            summary = "Fetch transaction types  REST API",
            description = "REST API to fetch all transaction types"
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
    public TransactionTypeResponse findAll(){
        return transactionTypeService.findAll();
    }

    @Operation(
            summary = "Fetch transaction type details REST API",
            description = "REST API to fetch transaction type details based on a name"
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
    @GetMapping("/{name}")
    public TransactionTypeResponse findByName(@PathVariable("name") String name){
        return transactionTypeService.findTransactionTypeByName(name);
    }

    @Operation(
            summary = "Add transaction type REST API",
            description = "REST API to add new transaction type inside Coffee App"
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
    public ResponseEntity<ResponseDto> addTransactionType(@Valid @RequestBody TransactionTypeDto transactionTypeDto){
        transactionTypeService.addTransactionType(transactionTypeDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(TransactionTypeConstants.MESSAGE_201, TransactionTypeConstants.MESSAGE_201));
    }

}
