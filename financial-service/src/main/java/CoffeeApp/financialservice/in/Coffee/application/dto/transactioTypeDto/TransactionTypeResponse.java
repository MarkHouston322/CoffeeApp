package CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All transaction types",
        description = "Schema to fetch all transaction type"
)
public class TransactionTypeResponse {

    private List<TransactionTypeDto> transactionTypes;
}
