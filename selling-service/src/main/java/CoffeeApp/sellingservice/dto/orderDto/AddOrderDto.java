package CoffeeApp.sellingservice.dto.orderDto;

import CoffeeApp.sellingservice.models.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddOrderDto {

    @NotNull(message = "Payment method should be empty")
    private PaymentMethod paymentMethod;
}
