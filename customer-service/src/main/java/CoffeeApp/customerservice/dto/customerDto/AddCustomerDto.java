package CoffeeApp.customerservice.dto.customerDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Schema(
        name = "Add Customer",
        description = "Schema to add new Customer"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCustomerDto {

    @Schema(
            description = "Name of Coffee App customer", example = "Denis"
    )
    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Schema(
            description = "Second name of Coffee App customer", example = "Basov"
    )
    @NotEmpty(message = "Second name should not be empty")
    private String secondName;

    @Schema(
            description = "Mobile number of Coffee App customer", example = "9998117351"
    )
    @NotEmpty(message = "Mobile number should not be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Email address of Coffee App customer", example = "biofiniy@gamil.com"
    )
    @Email(message = "Email address should be valid")
    private String email;


}
