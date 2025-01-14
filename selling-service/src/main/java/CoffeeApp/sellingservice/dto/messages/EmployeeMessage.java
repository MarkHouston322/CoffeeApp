package CoffeeApp.sellingservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Employee",
        description = "Schema to send an Employee"
)
public class EmployeeMessage {

    @Schema(
            description = "Name of Coffee App employee", example = "denis"
    )
    private String employeeName;

    @Schema(
            description = "Sum of Coffee App order processed by employee ", example = "500"
    )
    private Integer sum;
}
