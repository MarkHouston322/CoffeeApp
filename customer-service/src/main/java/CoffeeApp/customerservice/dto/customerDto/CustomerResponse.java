package CoffeeApp.customerservice.dto.customerDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Schema(
        name = "All Customers",
        description = "Schema to fetch all Customers"
)
@Data
@AllArgsConstructor
public class CustomerResponse {

    private List<CustomerDto> customers;
}
