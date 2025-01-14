package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionMessage {

    private Integer id;
    private String employeeUsername;
    private Boolean sessionIsClosed;
}
