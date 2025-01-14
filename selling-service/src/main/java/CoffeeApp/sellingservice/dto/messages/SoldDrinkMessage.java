package CoffeeApp.sellingservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "Send sold drink",
        description = "Schema to send a sold drink"
)
public class SoldDrinkMessage{

    @Schema(
            description = "Name of Coffee App drink", example = "Latte"
    )
    private String drinkName;

    @Schema(
            description = "Quantity of Coffee App order", example = "2"
    )
    private Integer quantity;

    @Schema(
            description = "Date and time of Coffee App order", example = "yyyy-MM-dd-HH-mm-ss.zzz"
    )
    private LocalDateTime time;
}
