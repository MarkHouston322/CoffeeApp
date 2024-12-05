package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerMessage {

    private Integer orderId;

    private Integer customerId;

    private Integer purchaseSum;
}
