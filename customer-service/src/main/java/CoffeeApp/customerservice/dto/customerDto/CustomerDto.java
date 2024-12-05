package CoffeeApp.customerservice.dto.customerDto;

import CoffeeApp.customerservice.models.LoyaltyLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Schema(
        name = "Customer",
        description = "Schema to hold Customer information"
)
@Data
public class CustomerDto {

    @Schema(
            description = "Name of Coffee App customer", example = "Denis"
    )
    private String name;

    @Schema(
            description = "Second name of Coffee App customer", example = "Basov"
    )
    private String secondName;

    @Schema(
            description = "Mobile number of Coffee App customer", example = "9998117351"
    )
    private String mobileNumber;

    @Schema(
            description = "Email address of Coffee App customer", example = "biofiniy@gamil.com"
    )
    private String email;

    @Schema(
            description = "Total purchases of Coffee App customer", example = "15000"
    )
    private Integer totalPurchases;

    @Schema(
            description = "Name of Coffee App customer loyalty level", example = "Beginner"
    )
    @JsonIgnoreProperties({"id","edge","customers"})
    private LoyaltyLevel loyaltyLevel;


}
