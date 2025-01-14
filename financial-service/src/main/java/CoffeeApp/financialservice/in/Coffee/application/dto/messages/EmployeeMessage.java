package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Customer",
        description = "Schema to send a Customer"
)
public class EmployeeMessage {

    @Schema(
            description = "Name of Coffee App employee", example = "denis"
    )
    private String employeeUsername;

    @Schema(
            description = "Flag of Coffee App session", example = "true"
    )
    private Boolean isClosed;
}
