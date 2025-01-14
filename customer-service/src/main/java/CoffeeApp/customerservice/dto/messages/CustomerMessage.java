package CoffeeApp.customerservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Customer",
        description = "Schema to send a Customer"
)
public class CustomerMessage {

    @Schema(
            description = "Id of Coffee App order", example = "1"
    )
    private Integer orderId;

    @Schema(
            description = "Id of Coffee App customer", example = "1"
    )
    private Integer customerId;

    @Schema(
            description = "Sum of Coffee App order", example = "500"
    )
    private Integer purchaseSum;
}
