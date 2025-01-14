package CoffeeApp.storageservice.dto.itemDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Schema(
        name = "Item in",
        description = "Schema to hold Item in other entities information"
)
public class ItemInDto {

    @Schema(
            description = "Name of Coffee App item", example = "Coca-Cola"
    )
    private String itemName;

    @Schema(
            description = "Cost price of Coffee App item", example = "50.0"
    )
    private Float costPrice;

    @Schema(
            description = "Quantity of Coffee App item", example = "20.0"
    )
    private Float quantity;
}
