package CoffeeApp.storageservice.dto.ingredientDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Ingredient In",
        description = "Schema to hold Ingredient in other entities information"
)
public class IngredientInDto {

    @Schema(
            description = "Name of Coffee App ingredient", example = "Milk"
    )
    private String ingredientName;

    @Schema(
            description = "Quantity of Coffee App customer", example = "1500.0"
    )
    private Float quantity;


}
