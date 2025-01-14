package CoffeeApp.storageservice.dto.acceptanceDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All Acceptances",
        description = "Schema to fetch all Acceptances"
)
public class AcceptanceResponse {

    private List<AcceptanceDto> acceptances;
}
