package CoffeeApp.financialservice.in.Coffee.application.dto.messages;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProceedSessionMessage {

    private LocalDateTime closingDate;
    private Integer totalMoney;
}
