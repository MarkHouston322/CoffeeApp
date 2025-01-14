package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.messages.GoodMessage;
import CoffeeApp.storageservice.interfaces.ContainIngredients;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.dto.drinkDto.*;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.DrinkAlreadyExistsException;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.Drink;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import CoffeeApp.storageservice.repositories.DrinkRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientInDrinkService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DrinkService implements ContainIngredients {

    private final DrinkRepository drinkRepository;
    private final IngredientInDrinkService ingredientInDrinkService;
    private final IngredientService ingredientService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;
    private final StreamBridge streamBridge;

    public ShowDrinkDto findById(int id) {
        Drink drink = checkIfExists(id);
        return convertToShowDrinkDto(drink);
    }

    public DrinkResponse findDrinksByName(String name) {
        return new DrinkResponse(drinkRepository.findByNameStartingWith(name).stream().map(this::convertToShowDrinkDto)
                .collect(Collectors.toList()));
    }


    public DrinkResponse findAll() {
        return new DrinkResponse(drinkRepository.findAll().stream().map(this::convertToShowDrinkDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addDrink(AddDrinkDto drinkDto, Map<String, String> ingredients) throws ExecutionException, InterruptedException {
        Drink drinkToAdd = convertToDrink(drinkDto);
        Optional<Drink> optionalDrink = drinkRepository.findByName(drinkToAdd.getName());
        if (optionalDrink.isPresent()){
            throw new DrinkAlreadyExistsException("Drink has already been added with this name: " + drinkToAdd.getName());
        }
        checkGoods(ingredients,ingredientService,itemService);
        drinkToAdd.setCostPrice(ingredientService.calculateCost(ingredients));
        drinkToAdd.setPrice((int) Math.ceil((drinkToAdd.getCostPrice() * drinkToAdd.getSurchargeRatio()) / 10) * 10);
        drinkToAdd.setSoldQuantity(0);
        drinkToAdd.setWriteOffQuantity(0);
        drinkRepository.save(drinkToAdd);
        saveIngredientInDrink(drinkToAdd,ingredients);
        sendDrink(drinkToAdd);

    }

    @Transactional
    public void deleteDrinkById(int id) {
        checkIfExists(id);
        drinkRepository.deleteById(id);
    }

    @Transactional
    public boolean updateDrink(int id, AddDrinkDto addDrinkDto, Map<String, String> ingredients) {
        boolean isUpdated = false;
        if (!ingredients.isEmpty()) {
            checkIfExists(id);
            Drink updatedDrink = convertToDrink(addDrinkDto);
            updatedDrink.setId(id);
            ingredientInDrinkService.deleteIngredientInDrink(id);
            updatedDrink.setCostPrice(ingredientService.calculateCost(ingredients));
            updatedDrink.setPrice((int) Math.ceil((updatedDrink.getCostPrice() * updatedDrink.getSurchargeRatio()) / 10) * 10);
            setQuantitiesForUpdate(updatedDrink,id);
            drinkRepository.save(updatedDrink);
            saveIngredientInDrink(updatedDrink,ingredients);
            sendDrink(updatedDrink);
            isUpdated = true;
        }
        return isUpdated;
    }

    public List<IngredientInDto> getIngredientsByDrinkId(Integer drinkId) {
        List<IngredientProjection> projections = drinkRepository.findIngredientsByDrinkId(drinkId);

        return projections.stream()
                .map(projection -> new IngredientInDto(projection.ingredientName(), projection.ingredientQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void sellDrink(String name, int quantity) {
        Drink drinkToSell = checkForSelling(name);
        drinkToSell.setSoldQuantity(drinkToSell.getSoldQuantity() + quantity);
        decreaseIngredients(drinkToSell.getId(), quantity);
    }

    private void decreaseIngredients(Integer id, int quantity) {
        List<IngredientInDrink> ingredientsToDecrease = ingredientInDrinkService.findByDrink_Id(id);

        Map<Ingredient, Float> ingredientsQuantity = ingredientsToDecrease.stream()
                .collect(Collectors.toMap(
                        IngredientInDrink::getIngredient,
                        IngredientInDrink::getQuantity,
                        Float::sum
                ));

        ingredientsQuantity.forEach((ingredient, totalQuantity) ->
                ingredientService.decreaseIngredient(ingredient.getId(), totalQuantity * quantity)
        );
    }

    private void setQuantitiesForUpdate(Drink updatedDrink,Integer id){
        Drink drinkToBeUpdated = checkIfExists(id);
        updatedDrink.setSoldQuantity(drinkToBeUpdated.getSoldQuantity());
        updatedDrink.setWriteOffQuantity(drinkToBeUpdated.getWriteOffQuantity());
    }


    private void saveIngredientInDrink(Drink drink, Map<String, String> ingredients){
        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            Ingredient ingredient = ingredientService.findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            IngredientInDrink ingredientInDrink = new IngredientInDrink(ingredient, drink, quantity);
            ingredientInDrinkService.addIngredientInDrink(ingredientInDrink);
        }
    }

    private Drink checkIfExists(int id) {
        return drinkRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Drink", "id", Integer.toString(id))
        );
    }

    @Override
    public GoodsWrapperForWriteOff checkGoods(Map<String, String> ingredients, IngredientService ingredientService, ItemService itemService) throws ExecutionException, InterruptedException {
        return ContainIngredients.super.checkGoods(ingredients, ingredientService,itemService);
    }

    private Drink checkForSelling(String name){
        return drinkRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Drink", "name",name)
        );
    }

    private void sendDrink(Drink drink){
        GoodMessage drinkMessage = new GoodMessage(drink.getName(), drink.getPrice(), "drink");
        streamBridge.send("sendGood-out-0", drinkMessage);
    }

    private ShowDrinkDto convertToShowDrinkDto(Drink drink) {
        return modelMapper.map(drink, ShowDrinkDto.class);
    }

    private Drink convertToDrink(AddDrinkDto addDrinkDto) {
        return modelMapper.map(addDrinkDto, Drink.class);
    }

}
