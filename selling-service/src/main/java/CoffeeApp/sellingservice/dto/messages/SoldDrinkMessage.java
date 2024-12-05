package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SoldDrinkMessage{

    private String drinkName;

    private Integer quantity;

    private LocalDateTime time;
}
