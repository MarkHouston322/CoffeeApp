package CoffeeApp.storageservice.mappers;

import CoffeeApp.storageservice.dto.writeOffDto.WriteOffDto;
import CoffeeApp.storageservice.models.WriteOff;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WriteOffMapper {


    public static WriteOffDto mapToWriteOffDto(WriteOff writeOff){
        WriteOffDto writeOffDto = new WriteOffDto();
        writeOffDto.setId(writeOff.getId());
        writeOffDto.setDate(dateFormatter(writeOff.getDate()));
        writeOffDto.setAcceptance(writeOff.getAcceptance());
        writeOffDto.setReason(writeOff.getReason());
        writeOffDto.setTotal(writeOff.getTotal());
        return writeOffDto;
    }
    private static String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
