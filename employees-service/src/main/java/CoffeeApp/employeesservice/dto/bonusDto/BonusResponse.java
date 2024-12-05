package CoffeeApp.employeesservice.dto.bonusDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Schema(
        name = "All Bonuses",
        description = "Schema to fetch all Bonuses"
)
@Data
@AllArgsConstructor
public class BonusResponse {

    private List<BonusDto> bonuses;
}
