package CoffeeApp.storageservice.mappers;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.models.Acceptance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AcceptanceMapper {

    public static AcceptanceDto matToAcceptanceDto(Acceptance acceptance){
        AcceptanceDto acceptanceDto = new AcceptanceDto();
        acceptanceDto.setId(acceptance.getId());
        acceptanceDto.setDate(dateFormatter(acceptance.getDate()));
        acceptanceDto.setWriteOffs(acceptance.getWriteOffs());
        acceptanceDto.setComment(acceptance.getComment());
        acceptanceDto.setTotal(acceptance.getTotal());
        return acceptanceDto;
    }
    private static String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
