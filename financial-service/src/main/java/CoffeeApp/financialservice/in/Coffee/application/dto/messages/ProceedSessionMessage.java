package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Session",
        description = "Schema to send a Session"
)
public class ProceedSessionMessage {

    @Schema(
            description = "Closing date and time of Coffee App session", example = "yyyy-MM-dd-HH-mm-ss.zzz"
    )
    private LocalDateTime closingDate;

    @Schema(
            description = "Total money of Coffee App session", example = "30000"
    )
    private Integer totalMoney;
}
