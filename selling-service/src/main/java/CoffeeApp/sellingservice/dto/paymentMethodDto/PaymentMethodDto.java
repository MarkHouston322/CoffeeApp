package CoffeeApp.sellingservice.dto.paymentMethodDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Payment method",
        description = "Schema to hold and add Payment method information"
)
public class PaymentMethodDto {

    @Schema(
            description = "Name of Coffee App payment method", example = "Cash"
    )
    @NotEmpty(message = "Payment method name should not be empty")
    private String name;

}
