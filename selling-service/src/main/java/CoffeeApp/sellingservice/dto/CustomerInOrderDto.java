package CoffeeApp.sellingservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Customer in Order",
        description = "Schema to hold and add customers in orders"
)
public class CustomerInOrderDto {

    @Schema(
            description = "Id of Coffee App customer", example = "1"
    )
    private Integer id;

    @Schema(
            description = "Discount percentage of Coffee App customer", example = "0.1"
    )
    private Float  discountPercentage;

}
