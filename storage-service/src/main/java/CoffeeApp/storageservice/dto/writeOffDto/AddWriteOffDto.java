package CoffeeApp.storageservice.dto.writeOffDto;

import CoffeeApp.storageservice.models.Acceptance;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddWriteOffDto {

    @NotEmpty(message = "Reason of write-off should be mentioned")
    private String reason;

    @NotNull(message = "Acceptance should be mentioned")
    private Acceptance acceptance;
}
