package CoffeeApp.sellingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoodInOrderDto {

    private String goodName;

    private Integer quantity;


}
