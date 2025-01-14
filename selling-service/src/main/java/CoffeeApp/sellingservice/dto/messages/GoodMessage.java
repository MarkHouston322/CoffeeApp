package CoffeeApp.sellingservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodMessage {

    private String name;
    private Integer price;
    private String type;
}
