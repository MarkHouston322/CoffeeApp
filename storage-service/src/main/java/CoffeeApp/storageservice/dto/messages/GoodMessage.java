package CoffeeApp.storageservice.dto.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodMessage {

    private String name;
    private Integer price;
    private String type;
}
