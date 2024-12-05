package CoffeeApp.employeesservice.dto.positionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(
        name = "Add and fetch position",
        description = "Schema to add and fetch position of employee"
)
@Data
public class PositionDto {

    @Schema(
            description = "Name of Coffee App position", example = "CEO"
    )
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Schema(
            description = "Salary of Coffee App position", example = "3000.0"
    )
    @NotNull(message = "Salary should not be empty")
    @Positive(message = "Salary should be greater than 0")
    private Float salary;

    @Schema(
            description = "Royalty of Coffee App position", example = "0.15"
    )
    private Float royalty;

}
