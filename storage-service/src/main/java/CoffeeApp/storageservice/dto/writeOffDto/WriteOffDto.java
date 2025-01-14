package CoffeeApp.storageservice.dto.writeOffDto;

import CoffeeApp.storageservice.models.Acceptance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Customer",
        description = "Schema to hold Customer information"
)
public class WriteOffDto {

    @Schema(
            description = "Id of Coffee App write off", example = "1"
    )
    private Integer id;

    @Schema(
            description = "Date and time of Coffee App write off", example = "dd.MM.yyyy HH:mm"
    )
    private String  date;

    @Schema(
            description = "Acceptance associated with Coffee App write off"
    )
    @JsonIgnoreProperties({"writeOffs", "ingredients", "comment", "total"})
    private Acceptance acceptance;

    @Schema(
            description = "Reason of Coffee App write off", example = "Expired"
    )
    private String reason;

    @Schema(
            description = "Total sum  of Coffee App write off", example = "5000.0"
    )
    private Float total;
}
