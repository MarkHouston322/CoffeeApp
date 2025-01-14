package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ItemDto {

    private String name;

    private Integer price;

    private Float surchargeRatio;

    private Float costPrice;

    private Float quantityInStock;

    private Category category;

    public ItemDto(String name, Integer price, Float quantityInStock) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }
}
