package CoffeeApp.storageservice.dto.ingredientDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
@Data
@AllArgsConstructor
public class IngredientResponse {

    private List<IngredientDto> ingredients;
}
