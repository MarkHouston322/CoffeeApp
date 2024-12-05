package CoffeeApp.sellingservice.dto.messages;

import lombok.Data;

@Data
public class GoodMessage {

    private String name;
    private Integer price;
    private String type;
}
