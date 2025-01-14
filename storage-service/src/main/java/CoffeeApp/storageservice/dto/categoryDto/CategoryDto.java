package CoffeeApp.storageservice.dto.categoryDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CategoryDto {
    @NotEmpty(message = "Category name can not be a null or empty")
    private String name;

}
