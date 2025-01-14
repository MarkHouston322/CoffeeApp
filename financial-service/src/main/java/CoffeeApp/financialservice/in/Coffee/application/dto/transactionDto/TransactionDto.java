package CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto;

import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.models.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Schema(
        name = "Fetch transaction",
        description = "Schema to fetch transaction"
)
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    @Schema(
            description = "Proceed date and time of transaction", example = "2024-02-09 13:56:23.648067"
    )
    private String date;

    @Schema(
            description = "Type of the transaction"
    )
    @JsonIgnoreProperties({"id", "transactions"})
    private TransactionType type;

    @Schema(
            description = "Transaction sum of money", example = "3000"
    )
    private Integer sum;

    @JsonIgnore
    private Session session;
}
