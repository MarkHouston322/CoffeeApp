package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.item.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Item in fridge",
        description = "Schema to add Item in fridge information"
)
public class AddItemInFridgeDto {

    @Schema(
            description = "Item that should be placed to the fridge"
    )
    @NotNull
    private Item item;

    @Schema(
            description = "Storage time of Coffee App item. Set in hours", example = "48"
    )
    @NotNull
    private Long storageTime;
}
