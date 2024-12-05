package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.Category;
import lombok.Data;

@Data
public class ItemDto {

    private String name;

    private Integer price;

    private Float surchargeRatio;

    private Float costPrice;

    private Float quantityInStock;

    private Category category;
}
