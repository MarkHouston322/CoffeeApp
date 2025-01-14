package CoffeeApp.storageservice.dto.acceptanceDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Acceptance from file",
        description = "Schema to add Acceptance from file information"
)
public class AcceptanceFromFileDto {

    @Schema(
            description = "Name of Coffee App good in acceptance", example = "Water"
    )
    private String name;

    @Schema(
            description = "Price of Coffee App good in acceptance", example = "500.0"
    )
    private Float price;

    @Schema(
            description = "Qauntity of Coffee App good in acceptance", example = "10.5"
    )
    private Float quantity;

}
