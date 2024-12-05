package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderMessage {

    private Integer orderId;
    private String goodName;
    private Integer goodQuantity;
    private String goodType;
}
