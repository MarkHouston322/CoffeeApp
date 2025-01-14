package CoffeeApp.storageservice.dto.drinkDto;

import CoffeeApp.storageservice.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Add Customer",
        description = "Schema to add new Customer"
)
public class AddDrinkDto {

    @Schema(
            description = "Name of Coffee App drink", example = "Latte"
    )
    @NotEmpty(message = "Drink name should not be empty")
    @Size(max = 100, message = "Drink name should be less than 100 characters" )
    private String name;

    @Schema(
            description = "Category of Coffee App customer", example = "Coffee"
    )
    @NotNull(message = "Drink category should not be empty")
    private Category category;

    @Schema(
            description = "Surcharge ratio of Coffee App customer", example = "2.0"
    )
    @NotNull(message = "Surcharge ratio  should not be empty")
    @Positive(message = "Surcharge ratio should not be negative")
    private Float surchargeRatio;
}
