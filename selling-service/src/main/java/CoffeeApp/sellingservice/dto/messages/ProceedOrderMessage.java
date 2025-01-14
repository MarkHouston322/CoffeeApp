package CoffeeApp.sellingservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "Send Order",
        description = "Schema to send an Order"
)
public class ProceedOrderMessage {

    @Schema(
            description = "Date and Time of Coffee App order", example = "yyyy-MM-dd-HH-mm-ss.zzz"
    )
    private LocalDateTime time;

    @Schema(
            description = "Total sum of Coffee App order", example = "1000"
    )
    private Integer total;

    @Schema(
            description = "Discount sum of Coffee App order", example = "200"
    )
    private Integer discount;

    @Schema(
            description = "Total sum with discount of Coffee App order", example = "800"
    )
    private Integer totalWithDsc;
}
