package CoffeeApp.employeesservice.dto.positionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All positions",
        description = "Schema to fetch all positions"
)
public class PositionResponse {

    private List<PositionDto> positions;
}
