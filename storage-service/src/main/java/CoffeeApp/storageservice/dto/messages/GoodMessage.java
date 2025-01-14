package CoffeeApp.storageservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Send Good",
        description = "Schema to send a Good"
)
public class GoodMessage {

    @Schema(
            description = "Name of Coffee App good", example = "Water"
    )
    private String name;

    @Schema(
            description = "Price of Coffee App good", example = "100"
    )
    private Integer price;

    @Schema(
            description = "Type of Coffee App order", example = "item"
    )
    private String type;
}
