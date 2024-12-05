package CoffeeApp.sellingservice.functions;

import CoffeeApp.sellingservice.dto.CustomerInOrderDto;
import CoffeeApp.sellingservice.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class CustomerFunctions {

    @Bean
    public Consumer<CustomerInOrderDto> getCustomer(OrderService orderService){
        return orderService ::saveCustomerCache;
    }

    @Bean
    public Consumer<Integer> purchaseConfirmation(OrderService orderService){
        return orderService :: updateCustomerPurchaseConfirmation;
    }
}
