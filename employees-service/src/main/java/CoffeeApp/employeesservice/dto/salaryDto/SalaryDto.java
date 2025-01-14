package CoffeeApp.employeesservice.dto.salaryDto;

import CoffeeApp.employeesservice.models.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        name = "Fetch salary session",
        description = "Schema to fetch salary session"
)
@Data
@AllArgsConstructor
public class SalaryDto {

    @Schema(
            description = "Employee of Coffee App salary session"
    )
    @JsonIgnoreProperties({"id", "salaries"})
    private Employee employee;

    @Schema(
            description = "Open date and time  of Coffee App salary session", example = "2024-02-09 13:56:23.648067"
    )
    private LocalDateTime date;

    @Schema(
            description = "Fixed part of Coffee App salary session", example = "3000.0"
    )
    private Float salary;

    @Schema(
            description = "Revenue dependent part of Coffee App salary session", example = "750.0"
    )
    private Float royalty;

    @Schema(
            description = "Revenue dependent part of Coffee App salary session", example = "750.0"
    )
    private Integer bonus;

    @Schema(
            description = "Total of Coffee App salary session", example = "3750.0"
    )
    private Float total;

    @Schema(
            description = "Revenue during the specific salary session", example = "30000"
    )
    private Integer revenue;

    @Schema(
            description = "Flag that shows if the specific salary session is closed or not"
    )
    private Boolean isClosed;


}
