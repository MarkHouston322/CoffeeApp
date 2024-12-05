package CoffeeApp.storageservice.dto.itemDto;

import CoffeeApp.storageservice.models.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddItemDto {

    @NotEmpty(message = "Item name should not be empty")
    private String name;

    @NotNull(message = "Surcharge ratio  should not be empty")
    @Positive(message = "Surcharge ratio should not be negative")
    private Float surchargeRatio;

    @NotNull(message = "Surcharge ratio  should not be empty")
    @Positive(message = "Surcharge ratio should not be negative")
    private Float quantityInStock;

    @NotNull(message = "Cost price should not be empty")
    @Positive(message = "Cost price  should not be negative")
    private Float costPrice;

    
    private Category category;
}
