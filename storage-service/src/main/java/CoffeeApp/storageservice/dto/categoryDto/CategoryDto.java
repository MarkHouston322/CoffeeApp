package CoffeeApp.storageservice.dto.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(
        name = "Customer",
        description = "Schema to hold and add Customer information"
)
public class CategoryDto {

    @Schema(
            description = "Name of Coffee App category", example = "Drinks"
    )
    @NotEmpty(message = "Category name can not be a null or empty")
    private String name;

}
