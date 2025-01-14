package CoffeeApp.sellingservice.dto.paymentMethodDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Payment methods",
        description = "Schema to fetch all payment methods"
)
public class PaymentMethodResponse {

    private List<PaymentMethodDto> paymentMethods;
}
