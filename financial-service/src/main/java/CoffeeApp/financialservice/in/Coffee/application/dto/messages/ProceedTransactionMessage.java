package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Transaction",
        description = "Schema to send a Transaction"
)
public class ProceedTransactionMessage {

    @Schema(
            description = "Date and time of Coffee App transaction", example = "yyyy-MM-dd-HH-mm-ss.zzz"
    )
    private LocalDateTime date;

    @Schema(
            description = "Type of Coffee App transaction", example = "cash"
    )
    private String transactionType;

    @Schema(
            description = "Sum of Coffee App transaction", example = "500"
    )
    private Integer sum;
}
