package CoffeeApp.employeesservice.dto.employeeDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All employees",
        description = "Schema to fetch all employees"
)
public class EmployeeResponse {

    private List<EmployeeDto> employees;
}
