package CoffeeApp.sellingservice.dto.paymentMethodDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentMethodDto {

    @NotEmpty(message = "Payment method name should not be empty")
    private String name;

}
