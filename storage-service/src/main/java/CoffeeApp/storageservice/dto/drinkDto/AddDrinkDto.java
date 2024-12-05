package CoffeeApp.storageservice.dto.drinkDto;

import CoffeeApp.storageservice.models.Category;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddDrinkDto {

    @NotEmpty(message = "Drink name should not be empty") //, groups = FirstValidation.class
    @Size(max = 100, message = "Drink name should be less than 100 characters" )
    private String name;

    @NotNull(message = "Drink category should not be empty") //, groups = FirstValidation.class
    private Category category;

    @NotNull(message = "Surcharge ratio  should not be empty") //, groups = FirstValidation.class
    @Positive(message = "Surcharge ratio should not be negative") //, groups = FirstValidation.class
    private Float surchargeRatio;
}
