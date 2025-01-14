package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Item",
        description = "Schema to add Item information"
)
public class AddItemDto {

    @Schema(
            description = "Name of Coffee App item", example = "Water"
    )
    @NotEmpty(message = "Item name should not be empty")
    private String name;

    @Schema(
            description = "Surcharge ratio of Coffee App item", example = "2.0"
    )
    @NotNull(message = "Surcharge ratio  should not be empty")
    @Positive(message = "Surcharge ratio should not be negative")
    private Float surchargeRatio;

    @Schema(
            description = "Quantity in stock of Coffee App item", example = "50.0"
    )
    @NotNull(message = "Surcharge ratio  should not be empty")
    @Positive(message = "Surcharge ratio should not be negative")
    private Float quantityInStock;

    @Schema(
            description = "Cost price of Coffee App item", example = "50.0"
    )
    @NotNull(message = "Cost price should not be empty")
    @Positive(message = "Cost price  should not be negative")
    private Float costPrice;

    @Schema(
            description = "Category of Coffee App item", example = "Drinks"
    )
    private Category category;
}
