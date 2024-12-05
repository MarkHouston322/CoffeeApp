package CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All transactions",
        description = "Schema to fetch all transactions"
)
public class TransactionsResponse {

    private List<TransactionDto> transactions;
}
