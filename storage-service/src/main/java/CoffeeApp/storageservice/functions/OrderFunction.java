package CoffeeApp.storageservice.functions;

import CoffeeApp.storageservice.dto.messages.OrderMessage;
import CoffeeApp.storageservice.services.DrinkService;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class OrderFunction {

    @Bean
    public Function<OrderMessage, Integer> sellGood(DrinkService drinkService, ItemService itemService, ItemInFridgeService itemInFridgeService){
        return orderMessage -> {
            if (orderMessage.getGoodType().equals("drink")){
                drinkService.sellDrink(orderMessage.getGoodName(), orderMessage.getGoodQuantity());
            } else if (orderMessage.getGoodType().equals("item")) {
                itemService.decreaseItem(orderMessage.getGoodName(), Float.valueOf(orderMessage.getGoodQuantity()));
                itemInFridgeService.removeItemsFromFridgeIfExists(orderMessage.getGoodName());
            }
            return orderMessage.getOrderId();
        };
    }
}
