package CoffeeApp.storageservice.dto.messages;

import lombok.Data;

@Data
public class OrderMessage {

    private Integer orderId;
    private String goodName;
    private Integer goodQuantity;
    private String goodType;
}
