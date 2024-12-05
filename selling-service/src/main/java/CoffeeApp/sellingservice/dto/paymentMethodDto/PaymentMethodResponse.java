package CoffeeApp.sellingservice.dto.paymentMethodDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentMethodResponse {

    private List<PaymentMethodDto> paymentMethods;
}
