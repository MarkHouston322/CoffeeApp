package CoffeeApp.sellingservice.dto.messages;

import lombok.Data;

@Data
public class SessionMessage {

    private Integer id;
    private String employeeUsername;
    private Boolean sessionIsClosed;
}
