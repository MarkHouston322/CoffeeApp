package CoffeeApp.storageservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
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
            description = "Quantity of Coffee App order", example = "2"
    )
    private Integer goodQuantity;

    @Schema(
            description = "Type of Coffee App good", example = "item"
    )
    private String goodType;
}
