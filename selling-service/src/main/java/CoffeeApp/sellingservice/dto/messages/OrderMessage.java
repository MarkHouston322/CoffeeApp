package CoffeeApp.sellingservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Order",
        description = "Schema to send an Order"
)
public class OrderMessage {

    @Schema(
            description = "Id of Coffee App order", example = "1"
    )
    private Integer orderId;

    @Schema(
            description = "Name of Coffee App good", example = "Water"
    )
    private String goodName;

    @Schema(
            description = "Quantity of Coffee App good", example = "5"
    )
    private Integer goodQuantity;

    @Schema(
            description = "Type of Coffee App good", example = "item"
    )
    private String goodType;
}
