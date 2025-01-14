package CoffeeApp.storageservice.dto.drinkDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Drinks",
        description = "Schema to fetch all Drinks"
)
public class DrinkResponse {

    private List<ShowDrinkDto> drinks;
}
