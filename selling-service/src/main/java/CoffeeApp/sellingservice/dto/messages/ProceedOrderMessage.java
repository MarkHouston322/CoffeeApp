package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProceedOrderMessage {

    private LocalDateTime time;

    private Integer total;

    private Integer discount;

    private Integer totalWithDsc;
}
