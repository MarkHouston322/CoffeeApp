package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.item.Item;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ItemInFridgeDto {

    private Item item;

    private Date placeDate;

    private Long storageTime;

    private Boolean isSold;

    private LocalDateTime soldDate;

    private Boolean expired;

}
