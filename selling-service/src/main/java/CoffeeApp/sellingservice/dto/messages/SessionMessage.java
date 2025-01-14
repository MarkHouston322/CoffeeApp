package CoffeeApp.sellingservice.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Send Session",
        description = "Schema to send a Session"
)
public class SessionMessage {

    @Schema(
            description = "Id of Coffee App session", example = "1"
    )
    private Integer id;

    @Schema(
            description = "Name of Coffee App employee", example = "denis"
    )
    private String employeeUsername;

    @Schema(
            description = "Flag is session is closed of Coffee App order", example = "true"
    )
    private Boolean sessionIsClosed;
}
