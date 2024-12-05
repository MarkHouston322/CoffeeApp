package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceFromFileDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceResponse;
import CoffeeApp.storageservice.dto.acceptanceDto.AddAcceptanceDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.dto.messages.GoodMessage;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.interfaces.ContainIngredients;
import CoffeeApp.storageservice.mappers.AcceptanceMapper;
import CoffeeApp.storageservice.models.Acceptance;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInAcceptance;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInAcceptance;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import CoffeeApp.storageservice.repositories.AcceptanceRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientInAcceptanceService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemInAcceptanceService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import CoffeeApp.storageservice.util.WordDocumentParser;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AcceptanceService implements ContainIngredients {

    private final AcceptanceRepository acceptanceRepository;
    private final IngredientService ingredientService;
    private final ItemService itemService;
    private final IngredientInAcceptanceService ingredientInAcceptanceService;
    private final ItemInAcceptanceService itemInAcceptanceService;
    private final ModelMapper modelMapper;
    private final WordDocumentParser wordDocumentParser;
    private final CoffeeApp.storageservice.util.AcceptanceMapper acceptanceMapper;
    private final StreamBridge streamBridge;


    public AcceptanceDto findById(Integer id){
        Acceptance acceptance = checkIfExists(id);
        return AcceptanceMapper.matToAcceptanceDto(acceptance);
    }

    public AcceptanceResponse findAll(){
        return new AcceptanceResponse(acceptanceRepository.findAll().stream().map(this::convertToAcceptanceDto)
                .collect(Collectors.toList()));
    }

    public List<IngredientInDto> getIngredientByAcceptanceId(Integer acceptanceId){
        List<IngredientProjection> projections = acceptanceRepository.findIngredientsByAcceptanceId(acceptanceId);

        return projections.stream()
                .map(projection -> new IngredientInDto(projection.ingredientName(), projection.ingredientQuantity()))
                .collect(Collectors.toList());
    }

    public List<ItemInDto> getItemsByAcceptanceId (Integer acceptanceId){
        List<ItemProjection> projections = acceptanceRepository.findItemsByAcceptanceId(acceptanceId);
        return projections.stream()
                .map(projection -> new ItemInDto(projection.itemName(), projection.itemQuantity(), projection.itemCostPrice()))
                .collect(Collectors.toList());
    }


    @Transactional
    public void addAcceptance(AddAcceptanceDto addAcceptanceDto, Map<String, String> goodsToAccept) throws ExecutionException, InterruptedException {
        Acceptance acceptanceToAdd = convertToAcceptance(addAcceptanceDto);
        GoodsWrapperForWriteOff goods = checkGoods(goodsToAccept,ingredientService,itemService);
        acceptanceToAdd.setTotal(ingredientService.calculateCost(goods.ingredientResults()));
        acceptanceToAdd.setTotal(acceptanceToAdd.getTotal() + itemService.calculateCost(goods.itemResults()));
        acceptanceToAdd.setDate(LocalDateTime.now());
        acceptanceRepository.save(acceptanceToAdd);
        saveIngredientInAcceptance(acceptanceToAdd,goods.ingredientResults());
        saveItemInAcceptance(acceptanceToAdd,goods.itemResults());
        increaseIngredients(goods.ingredientResults());
        increaseItems(goods.itemResults());

    }

    @Transactional
    public void addAcceptanceFromFileV2(MultipartFile file, Map<String, String> isIngredient, String surchargeRatio){
        List<AcceptanceFromFileDto> goods = wordDocumentParser.parseDocument(file);
        List<AddItemDto> items = new ArrayList<>();
        for (Map.Entry<String,String> entry: isIngredient.entrySet()){
            int index = Integer.parseInt(entry.getKey());
            boolean value = Boolean.parseBoolean(entry.getValue());
            AcceptanceFromFileDto good = goods.get(index);
            if (!value){
                items.add(acceptanceMapper.convertToAddItemInDto(good,surchargeRatio));
            }
        }
        checkItemsFromFile(items);
        Acceptance itemAcceptance = new Acceptance(LocalDateTime.now(),"", itemService.calculateCostFromFile(items));
        acceptanceRepository.save(itemAcceptance);
        saveItemInAcceptanceFromFile(itemAcceptance, items);
        sendItems(items);
    }

    private void sendItems(List<AddItemDto> items){
        for (AddItemDto item : items){
            GoodMessage itemMessage = new GoodMessage(item.getName(), (int) (item.getCostPrice() * item.getSurchargeRatio()),"item");
            streamBridge.send("sendGood-out-0", itemMessage);
        }
    }


    private Acceptance checkIfExists(int id) {
        return acceptanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", Integer.toString(id))
        );
    }

    @Override
    public GoodsWrapperForWriteOff checkGoods(Map<String, String> goods, IngredientService ingredientService, ItemService itemService) throws ExecutionException, InterruptedException {
        return ContainIngredients.super.checkGoods(goods, ingredientService,itemService);
    }


    private void checkItemsFromFile(List<AddItemDto> items){
        for (AddItemDto item : items){
            try {
                itemService.findByName(item.getName());
                itemService.increaseItem(item.getName(),item.getQuantityInStock());
            } catch (ResourceNotFoundException e){
                itemService.addItem(item);
            }

//            Item optionalItem = itemService.findByName(item.getName());
//            if (optionalItem.isPresent()){
//                itemService.increaseItem(item.getName(),item.getQuantityInStock());
//            } else {
//                itemService.addItem(item);
//            }
        }
    }

    private void saveIngredientInAcceptance(Acceptance acceptance, Map<String, String> ingredients){
        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            Ingredient ingredient = ingredientService.findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            IngredientInAcceptance ingredientInAcceptance = new IngredientInAcceptance(acceptance, ingredient, quantity);
            ingredientInAcceptanceService.addIngredientInAcceptance(ingredientInAcceptance);
        }
    }

    private void saveItemInAcceptance(Acceptance acceptance, Map<String, String> items){
        for (Map.Entry<String, String> entry : items.entrySet()) {
            Item item = itemService.findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            ItemInAcceptance itemInAcceptance = new ItemInAcceptance(acceptance, item, quantity);
            itemInAcceptanceService.addItemInAcceptance(itemInAcceptance);
        }
    }

    private void saveItemInAcceptanceFromFile(Acceptance acceptance, List<AddItemDto> items){
        for (AddItemDto item : items){
            Item optionalItem =   itemService.findByName(item.getName());
            float quantity = item.getQuantityInStock();
            ItemInAcceptance itemInAcceptance = new ItemInAcceptance(acceptance,optionalItem, quantity);
            itemInAcceptanceService.addItemInAcceptance(itemInAcceptance);
        }
    }

    private void increaseIngredients(Map<String, String> ingredients){
        for (Map.Entry<String, String> entry : ingredients.entrySet()){
            ingredientService.increaseIngredients(entry.getKey(), Float.parseFloat(entry.getValue()));
        }
    }

    private void increaseItems(Map<String, String> items){
        for (Map.Entry<String, String> entry : items.entrySet()){
            itemService.increaseItem(entry.getKey(), Float.parseFloat(entry.getValue()));
        }
    }

    private AcceptanceDto convertToAcceptanceDto(Acceptance acceptance){
        return modelMapper.map(acceptance, AcceptanceDto.class);
    }

    private Acceptance convertToAcceptance(AddAcceptanceDto addAcceptanceDto){
        return modelMapper.map(addAcceptanceDto, Acceptance.class);
    }

}
