package CoffeeApp.storageservice.dto.acceptanceDto;

import CoffeeApp.storageservice.models.WriteOff;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Acceptance",
        description = "Schema to hold Acceptance information"
)
public class AcceptanceDto {

    @Schema(
            description = "Id of Coffee App acceptance", example = "1"
    )
    private Integer id;

    @Schema(
            description = "Id of Coffee App acceptance", example = "dd.MM.yyyy HH:mm"
    )
    private String date;


    @Schema(
            description = "Comment of Coffee App acceptance", example = "OOO.RedOctober"
    )
    private String comment;

    @JsonIgnoreProperties({"ingredients","acceptance","reason","total"})
    private List<WriteOff> writeOffs;


    @Schema(
            description = "Total of Coffee App acceptance", example = "1500.0"
    )
    private Float total;

}
