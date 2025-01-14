package CoffeeApp.storageservice.dto.ingredientDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Ingredient",
        description = "Schema to hold and add Ingredient information"
)
public class IngredientDto {

    @Schema(
            description = "Name of Coffee App ingredient", example = "Milk"
    )
    @NotEmpty(message = "Ingredient name should not be empty")
    @Size(max = 100, message = "Ingredient name should not be less than 100 characters")
    private String name;

    @Schema(
            description = "Cost per one kg of Coffee App ingredient", example = "120.0"
    )
    @NotNull(message = "Ingredient cost per 1 kilo should not be empty")
    @Positive(message = "Ingredient cost per 1 kilo should not be negative")
    @Min(value = 0, message = "Ingredient cost per 1 kilo should be greater than 0")
    private Float costPerOneKilo;

    @Schema(
            description = "Quantity in stock of Coffee App ingredient", example = "2000.0"
    )
    @NotNull(message = "Ingredient quantity in stock should not be empty")
    @Positive(message = "Ingredient quantity in stock should not be negative")
    private Float quantityInStock;
}
