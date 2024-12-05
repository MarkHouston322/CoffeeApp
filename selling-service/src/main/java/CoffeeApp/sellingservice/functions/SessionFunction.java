package CoffeeApp.sellingservice.functions;

import CoffeeApp.sellingservice.dto.messages.SessionMessage;
import CoffeeApp.sellingservice.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SessionFunction {

    @Bean
    public Consumer<SessionMessage> getSession(OrderService orderService){
        return orderService :: saveSessionCache;
    }
}
