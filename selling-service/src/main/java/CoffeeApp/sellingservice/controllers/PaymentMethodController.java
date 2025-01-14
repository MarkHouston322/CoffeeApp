package CoffeeApp.sellingservice.controllers;

import CoffeeApp.sellingservice.constants.PaymentMethodConstants;
import CoffeeApp.sellingservice.dto.ErrorResponseDto;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodResponse;
import CoffeeApp.sellingservice.dto.ResponseDto;
import CoffeeApp.sellingservice.services.PaymentMethodService;
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
@AllArgsConstructor
@RequestMapping("/payment")
@Tag(
        name = "CRUD REST APIs for Payment methods in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE payment method details"
)
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;


    @Operation(
            summary = "Fetch  all Payment methods REST API",
            description = "REST API to fetch all Payment methods"
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
    public PaymentMethodResponse findAll() {
        return paymentMethodService.findAll();
    }

    @Operation(
            summary = "Fetch Payment method Details REST API",
            description = "REST API to fetch Payment method details based on an id"
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
    public ResponseEntity<PaymentMethodDto> getPaymentMethod(@PathVariable("id") Integer id) {
        PaymentMethodDto paymentMethodDto = paymentMethodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(paymentMethodDto);
    }

    @Operation(
            summary = "Fetch Payment methods Details REST API",
            description = "REST API to fetch Payment methods details based on a name"
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
    @GetMapping("/name/{name}")
    public PaymentMethodResponse findPaymentMethodByName(@PathVariable("name") String name) {
        return paymentMethodService.findByName(name);
    }

    @Operation(
            summary = "Add Payment method REST API",
            description = "REST API to add new Payment method inside Coffee App"
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
    public ResponseEntity<ResponseDto> addPaymentMethod(@Valid @RequestBody PaymentMethodDto paymentMethodDto) {
        paymentMethodService.addPaymentMethod(paymentMethodDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(PaymentMethodConstants.STATUS_201, PaymentMethodConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Payment method Details REST API",
            description = "REST API to update Payment method details based on an id"
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
    public ResponseEntity<ResponseDto> updatePaymentMethod(@PathVariable("id") Integer id,
                                                           @Valid @RequestBody PaymentMethodDto paymentMethodDto) {
        paymentMethodService.updatePaymentMethod(id, paymentMethodDto);
        return responseStatusOk();

    }

    @Operation(
            summary = "Delete Payment method Details REST API",
            description = "REST API to delete Payment method details based on an id"
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
    public ResponseEntity<ResponseDto> deletePaymentMethod(@PathVariable("id") Integer id) {
        paymentMethodService.deletePaymentMethod(id);
        return responseStatusOk();
    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(PaymentMethodConstants.STATUS_200, PaymentMethodConstants.MESSAGE_200));
    }
}
