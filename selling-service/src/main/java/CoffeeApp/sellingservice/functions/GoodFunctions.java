package CoffeeApp.sellingservice.functions;

import CoffeeApp.sellingservice.dto.messages.GoodMessage;
import CoffeeApp.sellingservice.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class GoodFunctions {

    @Bean
    public Consumer<GoodMessage> getGood(OrderService orderService) {
        return orderService::saveGoodCache;
    }

    @Bean
    public Consumer<Integer> sellConfirmation(OrderService orderService){
        return orderService::updateWriteOffConfirmation;

    }
}
