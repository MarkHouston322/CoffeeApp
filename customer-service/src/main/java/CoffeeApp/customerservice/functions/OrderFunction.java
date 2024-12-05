package CoffeeApp.customerservice.functions;

import CoffeeApp.customerservice.dto.messages.CustomerMessage;
import CoffeeApp.customerservice.services.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class OrderFunction {

    @Bean
    public Function<CustomerMessage, Integer> customerPurchase(CustomerService customerService){
        return customerMessage -> {
            customerService.increaseTotalPurchase(customerMessage.getCustomerId(), customerMessage.getPurchaseSum());
            return customerMessage.getOrderId();
        };
    }
}
