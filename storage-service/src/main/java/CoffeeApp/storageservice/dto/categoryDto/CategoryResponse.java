package CoffeeApp.storageservice.dto.categoryDto;

import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.List;
@Data
@AllArgsConstructor
public class CategoryResponse {

    private List<CategoryDto> categories;
}
