package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.item.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@Schema(
        name = "Item in fridge",
        description = "Schema to hold Item in fridge information"
)
public class ItemInFridgeDto {

    @Schema(
            description = "Item which in fridge"
    )
    private Item item;

    @Schema(
            description = "Date of item placement in fridge", example = "yyyy-MM-dd"
    )
    private Date placeDate;

    @Schema(
            description = "Storage time of Coffee App item. Set in hours", example = "48"
    )
    private Long storageTime;

    @Schema(
            description = "Flag is item is sold in fridge", example = "true"
    )
    private Boolean isSold;

    @Schema(
            description = "Sold date and time of Coffee App item", example = "dd.MM.yyyy HH:mm"
    )
    private LocalDateTime soldDate;

    @Schema(
            description = "Flag is item is expired in fridge", example = "false"
    )
    private Boolean expired;
}
