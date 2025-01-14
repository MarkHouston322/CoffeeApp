package CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(
        name = "Fetch total finance info",
        description = "Schema to fetch salary total finance info"
)
@AllArgsConstructor
public class TotalFinanceDto {

    @Schema(
            description = "Amount of cash payment through all the time", example = "100000"
    )
    private Integer cash;

    @Schema(
            description = "Amount of card payment through all the time", example = "100000"
    )
    private Integer card;
}
