package CoffeeApp.storageservice.dto.writeOffDto;

import CoffeeApp.storageservice.models.Acceptance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffDto {

    private Integer id;

    private String  date;

    @JsonIgnoreProperties({"writeOffs", "ingredients", "comment", "total"})
    private Acceptance acceptance;

    private String reason;

    private Float total;
}
