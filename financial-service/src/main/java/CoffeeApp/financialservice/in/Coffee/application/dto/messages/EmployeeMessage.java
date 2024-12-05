package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeMessage {

    private String employeeUsername;

    private Boolean isClosed;
}
