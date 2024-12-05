package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.constants.TransactionConstants;
import CoffeeApp.financialservice.in.Coffee.application.dto.ErrorResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.AddTransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.ResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionsResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.TransactionsService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
@Valid
@Tag(
        name = "CRUD REST APIs for transactions in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE and FETCH transactions"
)
public class TransactionController {

    private final TransactionsService transactionsService;

    @Operation(
            summary = "Fetch  all transactions  REST API",
            description = "REST API to fetch all financial transactions"
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
    public TransactionsResponse findAll(){
        return transactionsService.findAll();
    }

    @Operation(
            summary = "Fetch transaction details REST API",
            description = "REST API to fetch transaction details based on an id"
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
    public ResponseEntity<TransactionDto> findById(@PathVariable("id") Integer id){
        TransactionDto transactionDto = transactionsService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(transactionDto);
    }

    @Operation(
            summary = "Fetch transaction details REST API",
            description = "REST API to fetch transaction details based on a type"
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
    @GetMapping("/transactionType/{type}")
    public TransactionsResponse findByType(@PathVariable("type") String typeName){
        return transactionsService.findByType(typeName);
    }

    @Operation(
            summary = "Create transaction REST API",
            description = "REST API to create new cash inflow transaction"
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
    @PostMapping("/cashInflow")
    public ResponseEntity<ResponseDto> createCashInflow(@Valid @RequestBody AddTransactionDto addTransactionDto){
        transactionsService.createCashInflow(addTransactionDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(TransactionConstants.STATUS_201, TransactionConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Create transaction REST API",
            description = "REST API to create new cash withdrawal transaction"
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
    @PostMapping("/cashWithdrawal")
    public ResponseEntity<ResponseDto> createWithdrawal(@Valid @RequestBody AddTransactionDto addTransactionDto){
        transactionsService.createCashWithdrawal(addTransactionDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(TransactionConstants.STATUS_201, TransactionConstants.MESSAGE_201));
    }
}
