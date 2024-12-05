package CoffeeApp.storageservice.dto.itemDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ItemInDto {

    private String itemName;

    private Float costPrice;

    private Float quantity;
}
