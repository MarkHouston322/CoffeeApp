package CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(
        name = "Fetch and add transaction type",
        description = "Schema to fetch and add transaction type"
)
@AllArgsConstructor
public class TransactionTypeDto {

    @Schema(
            description = "Name of transaction type", example = "denis"
    )
    @NotEmpty(message = "Transaction type name should not be empty")
    private String name;

}
