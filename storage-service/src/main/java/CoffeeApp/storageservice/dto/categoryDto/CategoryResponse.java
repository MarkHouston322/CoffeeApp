package CoffeeApp.storageservice.dto.categoryDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Customers",
        description = "Schema to fetch all Customers"
)
public class CategoryResponse {

    private List<CategoryDto> categories;
}
