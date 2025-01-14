package CoffeeApp.sellingservice.controllers;

import CoffeeApp.sellingservice.constants.OrderConstants;
import CoffeeApp.sellingservice.dto.goodDto.GoodInOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.AddOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderResponse;
import CoffeeApp.sellingservice.dto.ResponseDto;
import CoffeeApp.sellingservice.services.GoodInOrderService;
import CoffeeApp.sellingservice.services.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public OrderResponse findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrder(@PathVariable("id") Integer id) {
        OrderDto orderDto = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @GetMapping("/{id}/drinks")
    public ResponseEntity<List<GoodInOrderDto>> getDrinksInOrder(@PathVariable("id") Integer id){
        List<GoodInOrderDto> drinks = orderService.getGoodsInOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(drinks);
    }

    @PostMapping("/add/{customer_id}")
    public ResponseEntity<ResponseDto> addOrder(@PathVariable("customer_id") String customerId ,@Valid @RequestBody AddOrderDto addOrderDto
            , @RequestParam Map<String, String> drinks) throws JsonProcessingException {
        orderService.addOrder(addOrderDto, drinks, customerId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(OrderConstants.STATUS_201, OrderConstants.MESSAGE_201));
    }
}
