package CoffeeApp.storageservice.dto.acceptanceDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Add Acceptance",
        description = "Schema to add new Acceptance"
)
public class AddAcceptanceDto {

    @Schema(
            description = "Comment of Coffee App acceptance", example = "OOO.RedOctober"
    )
    private String comment;
}
