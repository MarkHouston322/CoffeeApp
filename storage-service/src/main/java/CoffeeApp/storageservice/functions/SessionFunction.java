package CoffeeApp.storageservice.functions;

import CoffeeApp.storageservice.dto.messages.SessionMessage;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SessionFunction {

    @Bean
    public Consumer<SessionMessage> getSession(ItemInFridgeService itemInFridgeService){
        return sessionMessage -> {
            if (!sessionMessage.getSessionIsClosed()){
                itemInFridgeService.checkForExpiration();
            }
        };
    }
}
