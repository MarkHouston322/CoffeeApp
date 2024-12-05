package CoffeeApp.customerservice.dto.customerDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Send Customer",
        description = "Schema to send Customer details to Selling service"
)
@Data
@AllArgsConstructor
public class CustomerInOrderDto {

    @Schema(
            description = "ID of Coffee App customer", example = "5"
    )
    private Integer id;

    @Schema(
            description = "Discount percentage of Coffee App customer", example = "0.09"
    )
    private Float  discountPercentage;
}
