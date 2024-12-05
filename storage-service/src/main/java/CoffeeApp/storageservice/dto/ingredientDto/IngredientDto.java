package CoffeeApp.storageservice.dto.ingredientDto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {

    @NotEmpty(message = "Ingredient name should not be empty")
    @Size(max = 100, message = "Ingredient name should not be less than 100 characters")
    private String name;

    @NotNull(message = "Ingredient cost per 1 kilo should not be empty")
    @Positive(message = "Ingredient cost per 1 kilo should not be negative")
    @Min(value = 0, message = "Ingredient cost per 1 kilo should be greater than 0")
    private Float costPerOneKilo;

    @NotNull(message = "Ingredient quantity in stock should not be empty")
    @Positive(message = "Ingredient quantity in stock should not be negative")
    private Float quantityInStock;
}
