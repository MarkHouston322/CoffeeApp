package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import lombok.Data;

@Data
public class SessionMessage {

    private Integer id;
    private String employeeUsername;
    private Boolean sessionIsClosed;
}
