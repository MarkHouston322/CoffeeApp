package CoffeeApp.customerservice.dto.loyaltyLevelDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Loyalty level",
        description = "Schema to add and to fetch Loyalty level"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyLevelDto {

    @Schema(
            description = "Name of Coffee App Loyalty level", example = "Beginner"
    )
    @NotEmpty(message = "Loyalty level name should not be empty")
    private String name;

    @Schema(
            description = "Money edge of Coffee App Loyalty level", example = "10000"
    )
    @NotNull(message = "Total purchases edge should not be empty")
    @Min(value = 0,message = "Total purchases edge should be positive")
    private Integer edge;

    @Schema(
            description = "Discount percentage of Coffee App Loyalty level", example = "0.09"
    )
    @NotNull(message = "Discount percentage should not be empty")
    @Positive(message = "Discount percentage should be positive")
    private Float  discountPercentage;
}
