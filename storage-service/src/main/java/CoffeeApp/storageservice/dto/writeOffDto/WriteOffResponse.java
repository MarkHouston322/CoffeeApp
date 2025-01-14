package CoffeeApp.storageservice.dto.writeOffDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Write offs",
        description = "Schema to fetch all Write offs"
)
public class WriteOffResponse {

    private List<WriteOffDto> writeOffs;
}
