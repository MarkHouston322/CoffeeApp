package CoffeeApp.storageservice.dto.writeOffDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WriteOffResponse {

    private List<WriteOffDto> writeOffs;
}
