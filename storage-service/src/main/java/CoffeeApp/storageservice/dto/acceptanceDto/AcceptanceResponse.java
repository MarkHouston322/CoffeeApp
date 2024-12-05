package CoffeeApp.storageservice.dto.acceptanceDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AcceptanceResponse {

    private List<AcceptanceDto> acceptances;
}
