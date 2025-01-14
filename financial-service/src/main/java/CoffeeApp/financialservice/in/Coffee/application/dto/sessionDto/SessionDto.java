package CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Schema(
        name = "Fetch financial session",
        description = "Schema to fetch salary Bonus"
)
public class SessionDto {

    @Schema(
            description = "Open date and time of financial session", example = "2024-02-09 13:56:23.648067"
    )
    private LocalDateTime openingDate;

    @Schema(
            description = "Employee username of financial session", example = "denis"
    )
    private String employeeUsername;

    @Schema(
            description = "Amount of cash in the beginning of the financial session", example = "1000"
    )
    private Integer initialCash;

    @Schema(
            description = "Cash inflow during the financial session", example = "1000"
    )
    private Integer cashInflow;

    @Schema(
            description = "Cash expenses during the financial session", example = "1000"
    )
    private Integer cashExpense;

    @Schema(
            description = "Card payments during the financial session", example = "1000"
    )
    private Integer cardPayment;

    @Schema(
            description = "Cash payments during the financial session", example = "1000"
    )
    private Integer cashPayment;

    @Schema(
            description = "Orders quantity during the financial session", example = "67"
    )
    private Integer ordersQuantity;

    @Schema(
            description = "Revenue during the financial session", example = "30000"
    )
    private Integer totalMoney;

    @Schema(
            description = "Amount of cash at the end of the financial session", example = "1000"
    )
    private Integer finiteCash;

    @Schema(
            description = "Close date and time of financial session", example = "2024-02-09 13:56:23.648067"
    )
    private LocalDateTime closingDate;

    @Schema(
            description = "Flag shows if financial session is closed or not"
    )
    private Boolean sessionIsClosed;

}
