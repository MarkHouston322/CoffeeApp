package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.item.Item;
import lombok.Data;

@Data
public class AddItemInFridgeDto {

    private Item item;

    private Long storageTime;

}
