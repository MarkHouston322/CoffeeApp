package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(
        name = "Item",
        description = "Schema to hold Item information"
)
public class ItemDto {

    @Schema(
            description = "Name of Coffee App item", example = "Coca-Cola"
    )
    private String name;

    @Schema(
            description = "Price of Coffee App item", example = "100"
    )
    private Integer price;

    @Schema(
            description = "Surcharge ratio of Coffee App item", example = "2.0"
    )
    private Float surchargeRatio;

    @Schema(
            description = "Cost price of Coffee App item", example = "50.0"
    )
    private Float costPrice;

    @Schema(
            description = "Quantity of Coffee App item", example = "20.0"
    )
    private Float quantityInStock;

    @Schema(
            description = "Category of Coffee App item", example = "Drinks"
    )
    private Category category;

    public ItemDto(String name, Integer price, Float quantityInStock) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }
}
