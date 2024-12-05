package CoffeeApp.storageservice.dto.itemDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemResponse {

    private List<ItemDto> items;
}
