package CoffeeApp.sellingservice.dto.orderDto;

import CoffeeApp.sellingservice.models.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrderDto {

    @NotNull(message = "Payment method should be empty")
    private PaymentMethod paymentMethod;
}
