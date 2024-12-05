package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMessage {

    private String transactionType;

    private Integer sum;
}
