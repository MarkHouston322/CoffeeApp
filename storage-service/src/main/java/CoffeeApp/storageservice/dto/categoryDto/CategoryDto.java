package CoffeeApp.storageservice.dto.categoryDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class CategoryDto {
    @NotEmpty(message = "Category name can not be a null or empty")
    private String name;

}
