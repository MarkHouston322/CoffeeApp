package CoffeeApp.sellingservice.dto.orderDto;

import CoffeeApp.sellingservice.models.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class OrderDto {

    private Integer id;

    private String orderDate;

    private Integer total;

    private Integer discount;

    private Integer totalWithDsc;

    @JsonIgnoreProperties({"id", "orders"})
    private PaymentMethod paymentMethod;

    private Integer customerId;

    private Integer sessionId;

    private String employeeName;

    private Boolean customerPurchaseConfirmation;


}
