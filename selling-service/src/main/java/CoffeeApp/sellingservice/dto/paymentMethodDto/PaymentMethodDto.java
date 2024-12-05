package CoffeeApp.sellingservice.dto.paymentMethodDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PaymentMethodDto {

    @NotEmpty(message = "Payment method name should not be empty")
    private String name;

}
