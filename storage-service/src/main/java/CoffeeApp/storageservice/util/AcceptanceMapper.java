package CoffeeApp.storageservice.util;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceFromFileDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientDto;
import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import org.springframework.stereotype.Component;

@Component
public class AcceptanceMapper {

    public IngredientDto convertToIngredientDto(AcceptanceFromFileDto acceptance){
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName(acceptance.getName());
        ingredientDto.setCostPerOneKilo(acceptance.getPrice());
        ingredientDto.setQuantityInStock(acceptance.getQuantity());
        return ingredientDto;
    }

    public AddItemDto convertToAddItemInDto(AcceptanceFromFileDto acceptance, String surchargeRatio){
        AddItemDto addItemDto = new AddItemDto();
        addItemDto.setName(acceptance.getName());
        addItemDto.setQuantityInStock(acceptance.getQuantity());
        addItemDto.setCostPrice(acceptance.getPrice());
        addItemDto.setSurchargeRatio(Float.parseFloat(surchargeRatio));
        return addItemDto;
    }
}
