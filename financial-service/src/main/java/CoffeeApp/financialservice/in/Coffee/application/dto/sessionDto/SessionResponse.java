package CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(
        name = "All financial sessions",
        description = "Schema to fetch all financial sessions"
)
public class SessionResponse {

    private List<SessionDto> dailyFinances;
}
