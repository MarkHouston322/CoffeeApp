package CoffeeApp.sellingservice.dto.orderDto;

import CoffeeApp.sellingservice.models.PaymentMethod;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Add Order",
        description = "Schema to add new Order"
)
public class AddOrderDto {

    @Schema(
            description = "Payment method of Coffee App order", example = "Cash"
    )
    @NotNull(message = "Payment method should be empty")
    private PaymentMethod paymentMethod;
}
