package CoffeeApp.employeesservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionMessage {

    private String employeeUsername;

    private Boolean isClosed;
}
