package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

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
            description = "Transaction type of Coffee App payment", example = "cash"
    )
    private String transactionType;

    @Schema(
            description = "Sum of Coffee App payment", example = "500"
    )
    private Integer sum;
}
