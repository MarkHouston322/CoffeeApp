package CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(
        name = "Fetch financial session",
        description = "Schema to add transactions"
)
@AllArgsConstructor
public class AddTransactionDto {

    @Schema(
            description = "Transaction value", example = "1000"
    )
    @NotNull(message = "Transaction value should not be empty")
    private Integer sum;
}
