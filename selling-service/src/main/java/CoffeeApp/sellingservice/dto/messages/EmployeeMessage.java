package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeMessage {

    private String employeeName;

    private Integer sum;
}
