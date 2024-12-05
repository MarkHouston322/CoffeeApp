package CoffeeApp.employeesservice.dto.salaryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All salary sessions",
        description = "Schema to fetch all salary sessions"
)
public class SalaryResponse {

    private List<SalaryDto> salaries;
}
