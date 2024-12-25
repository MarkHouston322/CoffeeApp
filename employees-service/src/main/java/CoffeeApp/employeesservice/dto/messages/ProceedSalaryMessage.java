package CoffeeApp.employeesservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProceedSalaryMessage {

    private LocalDateTime date;
    private String employeeName;
    private Float total;
}
