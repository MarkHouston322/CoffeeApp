package CoffeeApp.storageservice.dto.drinkDto;

import CoffeeApp.storageservice.models.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Drink",
        description = "Schema to show Drink information"
)
public class ShowDrinkDto {

    @Schema(
            description = "Name of Coffee App Drink", example = "Latte"
    )
    private String name;

    @Schema(
            description = "Price of Coffee App drink", example = "250"
    )
    private Integer price;

    @Schema(
            description = "Cost price of Coffee App drink", example = "125"
    )
    private Float costPrice;

    @Schema(
            description = "Category of Coffee App drink", example = "Coffee"
    )
    @JsonIgnoreProperties({"drinks", "id"})
    private Category category;

    @Schema(
            description = "Sold quantity of Coffee App drink", example = "50"
    )
    private Integer soldQuantity;

    @Schema(
            description = "Write-off quantity of Coffee App drink", example = "5"
    )
    private Integer writeOffQuantity;

    @Schema(
            description = "Surcharge ratio of Coffee App drink", example = "2.0"
    )
    private Float surchargeRatio;
}
