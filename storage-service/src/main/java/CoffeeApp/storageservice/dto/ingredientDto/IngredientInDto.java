package CoffeeApp.storageservice.dto.ingredientDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientInDto {

    private String ingredientName;
    private Float quantity;


}
