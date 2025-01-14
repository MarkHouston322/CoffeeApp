package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.item.Item;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddItemInFridgeDto {

    @NotNull
    private Item item;

    @NotNull
    private Long storageTime;
}
