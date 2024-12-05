package CoffeeApp.employeesservice.dto.employeeDto;

import CoffeeApp.employeesservice.models.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Fetch employee info",
        description = "Schema to fetch employee info information"
)
@Data
public class EmployeeDto {

    @Schema(
            description = "Username of Coffee App employee", example = "denis"
    )
    private String name;


    @JsonIgnoreProperties({"id","employees"})
    private Position position;

}
