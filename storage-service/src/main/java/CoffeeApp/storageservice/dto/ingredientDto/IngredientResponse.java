package CoffeeApp.storageservice.dto.ingredientDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Ingredients",
        description = "Schema to fetch all Ingredients"
)
public class IngredientResponse {

    private List<IngredientDto> ingredients;
}
