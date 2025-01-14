package CoffeeApp.sellingservice.dto.orderDto;

import CoffeeApp.sellingservice.models.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Order",
        description = "Schema to hold Order information"
)
public class OrderDto {

    @Schema(
            description = "Id of Coffee App order", example = "1"
    )
    private Integer id;

    @Schema(
            description = "Date and time of Coffee App order", example = "dd.MM.yyyy HH:mm"
    )
    private String orderDate;

    @Schema(
            description = "Total sum of Coffee App order", example = "1000"
    )
    private Integer total;

    @Schema(
            description = "Discount sum of Coffee App order", example = "200"
    )
    private Integer discount;

    @Schema(
            description = "Total sum with discount of Coffee App order", example = "800"
    )
    private Integer totalWithDsc;

    @Schema(
            description = "Payment method of Coffee App order", example = "Cash"
    )
    @JsonIgnoreProperties({"id", "orders"})
    private PaymentMethod paymentMethod;

    @Schema(
            description = "Id of Coffee App customer", example = "1"
    )
    private Integer customerId;

    @Schema(
            description = "Id of Coffee App session", example = "1"
    )
    private Integer sessionId;

    @Schema(
            description = "Name of Coffee App employee", example = "denis"
    )
    private String employeeName;

    @Schema(
            description = "Flag purchase is confirmed of Coffee App order", example = "true"
    )
    private Boolean customerPurchaseConfirmation;


}
