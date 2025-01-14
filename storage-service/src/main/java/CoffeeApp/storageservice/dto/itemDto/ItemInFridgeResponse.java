package CoffeeApp.storageservice.dto.itemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Items in fridge",
        description = "Schema to fetch all Items in fridge"
)
public class ItemInFridgeResponse {

    private List<ItemInFridgeDto> items;
}
