package CoffeeApp.employeesservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderMessage {

    private String employeeName;

    private Integer sum;
}
