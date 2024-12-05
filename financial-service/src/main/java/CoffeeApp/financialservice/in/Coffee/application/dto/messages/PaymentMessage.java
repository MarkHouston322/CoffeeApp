package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMessage {

    private String transactionType;

    private Integer sum;
}
