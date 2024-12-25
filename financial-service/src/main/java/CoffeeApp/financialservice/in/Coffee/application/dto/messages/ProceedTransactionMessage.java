package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProceedTransactionMessage {

    private LocalDateTime date;
    private String transactionType;
    private Integer sum;
}
