package CoffeeApp.storageservice.dto.writeOffDto;

import CoffeeApp.storageservice.models.Acceptance;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Write off",
        description = "Schema to add Write off information"
)
public class AddWriteOffDto {

    @Schema(
            description = "Reason of Coffee App write off", example = "Expired"
    )
    @NotEmpty(message = "Reason of write-off should be mentioned")
    private String reason;

    @Schema(
            description = "Acceptance associated with Coffee App write off"
    )
    @NotNull(message = "Acceptance should be mentioned")
    private Acceptance acceptance;
}
