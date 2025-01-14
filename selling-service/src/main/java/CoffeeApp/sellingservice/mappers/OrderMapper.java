package CoffeeApp.sellingservice.mappers;

import CoffeeApp.sellingservice.dto.orderDto.OrderDto;
import CoffeeApp.sellingservice.models.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderMapper {

    public static OrderDto mapToOrderDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(dateFormatter(order.getOrderDate()));
        orderDto.setTotal(order.getTotal());
        orderDto.setDiscount(order.getDiscount());
        orderDto.setTotalWithDsc(order.getTotalWithDsc());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setCustomerId(order.getCustomerId());
        orderDto.setSessionId(order.getSessionId());
        return orderDto;
    }

    private static String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
