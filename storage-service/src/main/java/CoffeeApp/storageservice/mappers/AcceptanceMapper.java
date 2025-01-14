package CoffeeApp.storageservice.mappers;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceFromFileDto;
import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import CoffeeApp.storageservice.models.Acceptance;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AcceptanceMapper {

    public static AcceptanceDto convertToAcceptanceDto(Acceptance acceptance){
        AcceptanceDto acceptanceDto = new AcceptanceDto();
        acceptanceDto.setId(acceptance.getId());
        acceptanceDto.setDate(dateFormatter(acceptance.getDate()));
        acceptanceDto.setWriteOffs(acceptance.getWriteOffs());
        acceptanceDto.setComment(acceptance.getComment());
        acceptanceDto.setTotal(acceptance.getTotal());
        return acceptanceDto;
    }

    public AddItemDto convertToAddItemInDto(AcceptanceFromFileDto acceptance, String surchargeRatio){
        AddItemDto addItemDto = new AddItemDto();
        addItemDto.setName(acceptance.getName());
        addItemDto.setQuantityInStock(acceptance.getQuantity());
        addItemDto.setCostPrice(acceptance.getPrice());
        addItemDto.setSurchargeRatio(Float.parseFloat(surchargeRatio));
        return addItemDto;
    }

    private static String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
