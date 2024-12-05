package CoffeeApp.customerservice.dto.loyaltyLevelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Schema(
        name = "All Loyalty levels",
        description = "Schema to fetch all Loyalty levels"
)
@Data
@AllArgsConstructor
public class LoyaltyLevelResponse {

    private List<LoyaltyLevelDto> loyaltyLevels;
}
