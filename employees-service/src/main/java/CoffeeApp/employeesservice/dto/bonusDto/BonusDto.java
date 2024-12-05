package CoffeeApp.employeesservice.dto.bonusDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Add and fetch salary Bonus",
        description = "Schema to add and fetch salary Bonus"
)
@Data
@AllArgsConstructor
public class BonusDto {

    @Schema(
            description = "Edge of Coffee App salary bonus", example = "10000"
    )
    @NotNull(message = "Bonus edge should not be empty")
    @Min(value = 0,message = "Bonus edge should be positive")
    private Integer edge;

    @Schema(
            description = "Value of Coffee App salary bonus", example = "500"
    )
    @NotNull(message = "Bonus value should not be empty")
    @Min(value = 0,message = "Bonus value should be positive")
    private Integer value;


}
