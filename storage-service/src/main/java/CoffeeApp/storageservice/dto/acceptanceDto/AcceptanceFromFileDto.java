package CoffeeApp.storageservice.dto.acceptanceDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptanceFromFileDto {

    private String name;

    private Float price;

    private Float quantity;

}
