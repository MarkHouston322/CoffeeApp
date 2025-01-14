package CoffeeApp.employeesservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Salary",
        description = "Schema to send a Salary"
)
public class ProceedSalaryMessage {

    @Schema(
            description = "Date and time of Coffee App salary", example = "yyyy-MM-dd-HH-mm-ss.zzz"
    )
    private LocalDateTime date;

    @Schema(
            description = "Name of Coffee App employee", example = "denis"
    )
    private String employeeName;

    @Schema(
            description = "Total sum of Coffee App salary", example = "3000.0"
    )
    private Float total;
}
