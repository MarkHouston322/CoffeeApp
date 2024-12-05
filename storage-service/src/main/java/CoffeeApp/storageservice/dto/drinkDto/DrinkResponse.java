package CoffeeApp.storageservice.dto.drinkDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
@Data
@AllArgsConstructor
public class DrinkResponse {

    private List<ShowDrinkDto> drinks;
}
