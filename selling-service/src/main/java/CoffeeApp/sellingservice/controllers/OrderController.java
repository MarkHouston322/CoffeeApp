package CoffeeApp.sellingservice.controllers;

import CoffeeApp.sellingservice.constants.OrderConstants;
import CoffeeApp.sellingservice.dto.ErrorResponseDto;
import CoffeeApp.sellingservice.dto.GoodInOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.AddOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderResponse;
import CoffeeApp.sellingservice.dto.ResponseDto;
import CoffeeApp.sellingservice.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Tag(
        name = "CRUD REST APIs for Orders in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE and FETCH AND order details"
)
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Fetch  all Orders REST API",
            description = "REST API to fetch all Orders"
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
    public OrderResponse findAll() {
        return orderService.findAll();
    }

    @Operation(
            summary = "Fetch Order Details REST API",
            description = "REST API to fetch Order details based on an id"
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
    public ResponseEntity<OrderDto> findOrder(@PathVariable("id") Integer id) {
        OrderDto orderDto = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @Operation(
            summary = "Fetch Order Details REST API",
            description = "REST API to fetch drinks in Order details based on an id"
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
    @GetMapping("/{id}/drinks")
    public ResponseEntity<List<GoodInOrderDto>> getDrinksInOrder(@PathVariable("id") Integer id){
        List<GoodInOrderDto> drinks = orderService.getGoodsInOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(drinks);
    }

    @Operation(
            summary = "Add Order REST API",
            description = "REST API to add new Order inside Coffee App"
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
    @PostMapping("/add/{customer_id}")
    public ResponseEntity<ResponseDto> addOrder(@PathVariable("customer_id") String customerId ,@Valid @RequestBody AddOrderDto addOrderDto
            , @RequestParam Map<String, String> drinks) throws JsonProcessingException {
        orderService.addOrder(addOrderDto, drinks, customerId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(OrderConstants.STATUS_201, OrderConstants.MESSAGE_201));
    }
}
