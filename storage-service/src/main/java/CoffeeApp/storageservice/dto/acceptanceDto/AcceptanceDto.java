package CoffeeApp.storageservice.dto.acceptanceDto;

import CoffeeApp.storageservice.models.WriteOff;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AcceptanceDto {

    private Integer id;

    private String date;

    private String comment;
    @JsonIgnoreProperties({"ingredients","acceptance","reason","total"})
    private List<WriteOff> writeOffs;

    private Float total;

    public AcceptanceDto(String date, String comment, Float total) {
        this.date = date;
        this.comment = comment;
        this.total = total;
    }
}
