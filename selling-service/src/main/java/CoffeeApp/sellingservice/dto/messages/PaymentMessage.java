package CoffeeApp.sellingservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Payment",
        description = "Schema to send a Payment"
)
public class PaymentMessage {

    @Schema(
            description = "Type of Coffee App transaction", example = "cash"
    )
    private String transactionType;

    @Schema(
            description = "Sum of Coffee App transaction", example = "500"
    )
    private Integer sum;
}
