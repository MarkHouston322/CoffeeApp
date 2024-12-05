package CoffeeApp.storageservice.dto.acceptanceDto;

import CoffeeApp.storageservice.models.WriteOff;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class AcceptanceDto {

    private Integer id;

    private String date;

    private String comment;
    @JsonIgnoreProperties({"ingredients","acceptance","reason","total"})
    private List<WriteOff> writeOffs;

    private Float total;
}
