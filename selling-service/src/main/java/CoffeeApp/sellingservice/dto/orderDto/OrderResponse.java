package CoffeeApp.sellingservice.dto.orderDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Orders",
        description = "Schema to fetch all Orders"
)
public class OrderResponse {

    private List<OrderDto> orders;
}
