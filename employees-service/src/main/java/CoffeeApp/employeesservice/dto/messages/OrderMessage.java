package CoffeeApp.employeesservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Order",
        description = "Schema to send an Order"
)
public class OrderMessage {

    @Schema(
            description = "Name of Coffee App employeeName", example = "denis"
    )
    private String employeeName;

    @Schema(
            description = "Sum of Coffee App order", example = "500"
    )
    private Integer sum;
}
