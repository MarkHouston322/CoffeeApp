package CoffeeApp.storageservice.dto.itemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Items",
        description = "Schema to fetch all Items"
)
public class ItemResponse {

    private List<ItemDto> items;
}
