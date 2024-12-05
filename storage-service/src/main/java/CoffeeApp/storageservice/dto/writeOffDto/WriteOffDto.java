package CoffeeApp.storageservice.dto.writeOffDto;

import CoffeeApp.storageservice.models.Acceptance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class WriteOffDto {

    private Integer id;

    private String  date;

    @JsonIgnoreProperties({"writeOffs", "ingredients", "comment", "total"})
    private Acceptance acceptance;

    private String reason;

    private Float total;
}
