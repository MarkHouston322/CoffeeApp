package CoffeeApp.storageservice.dto.drinkDto;

import CoffeeApp.storageservice.models.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class ShowDrinkDto {

    private String name;

    private Integer price;

    private Float costPrice;
    @JsonIgnoreProperties({"drinks", "id"})
    private Category category;

    private Integer soldQuantity;

    private Integer writeOffQuantity;

    private Float surchargeRatio;
}
