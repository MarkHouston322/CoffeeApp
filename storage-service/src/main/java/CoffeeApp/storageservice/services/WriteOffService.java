package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.dto.writeOffDto.AddWriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffResponse;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.interfaces.ContainGoods;
import CoffeeApp.storageservice.mappers.WriteOffMapper;
import CoffeeApp.storageservice.models.*;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInWriteOff;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInWriteOff;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import CoffeeApp.storageservice.repositories.WriteOffRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientInWriteOffService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
import CoffeeApp.storageservice.services.itemService.ItemInWriteOffService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class WriteOffService implements ContainGoods {

    private final WriteOffRepository writeOffRepository;
    private final IngredientService ingredientService;
    private final ItemService itemService;
    private final IngredientInWriteOffService ingredientInWriteOffService;
    private final ItemInWriteOffService itemInWriteOffService;
    private final ModelMapper modelMapper;
    private final WriteOffMapper writeOffMapper;
    private final ItemInFridgeService itemInFridgeService;


    public WriteOffDto findById(Integer id){
        WriteOff writeOff = checkIfExists(id);
        return convertToWriteOffDto(writeOff);
    }

    public WriteOffResponse findAll(){
        return new WriteOffResponse(writeOffRepository.findAll().stream().map(this::convertToWriteOffDto)
                .collect(Collectors.toList()));
    }

    public List<IngredientInDto> getIngredientsByWriteOffId(Integer writeOffId){
        List<IngredientProjection> projections = writeOffRepository.findIngredientsByWriteOffId(writeOffId);

        return projections.stream()
                .map(projection -> new IngredientInDto(projection.ingredientName(), projection.ingredientQuantity()))
                .collect(Collectors.toList());
    }

    public List<ItemInDto> getItemsByWriteOffId(Integer writeOffId){
        List<ItemProjection> projections = writeOffRepository.findItemsByWriteOffId(writeOffId);

        return projections.stream()
                .map(projection -> new ItemInDto(projection.itemName(), projection.itemQuantity(), projection.itemCostPrice()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addWriteOffGoods(AddWriteOffDto addWriteOffDto, Map<String, String> goodsForWriteOff) throws ExecutionException, InterruptedException {
        WriteOff writeOffToAdd = convertToWriteOff(addWriteOffDto);
        GoodsWrapperForWriteOff goods =  checkGoods(goodsForWriteOff,ingredientService, itemService);
        writeOffToAdd.setTotal(ingredientService.calculateCost(goods.ingredientResults()));
        writeOffToAdd.setTotal(writeOffToAdd.getTotal() + itemService.calculateCost(goods.itemResults()));
        writeOffToAdd.setDate(LocalDateTime.now());
        writeOffRepository.save(writeOffToAdd);
        saveGoodsInWriteOff(writeOffToAdd,goods);
        decreaseIngredients(goods.ingredientResults());
        decreaseItems(goods.itemResults());
        itemInFridgeService.writeOffItemsFromFridge(goods.itemResults());
    }



    private void saveGoodsInWriteOff(WriteOff writeOff, GoodsWrapperForWriteOff goods){
        ConcurrentHashMap<String, String> items = goods.itemResults();
        ConcurrentHashMap<String, String> ingredients = goods.ingredientResults();
        for (Map.Entry<String, String> entry : items.entrySet()) {
            Item item = itemService.findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            ItemInWriteOff itemInWriteOff = new ItemInWriteOff(writeOff, item, quantity);
            itemInWriteOffService.addItemInWriteOff(itemInWriteOff);
        }
        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            Ingredient ingredient = ingredientService.findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            IngredientInWriteOff ingredientInWriteOff = new IngredientInWriteOff(writeOff, ingredient, quantity);
            ingredientInWriteOffService.addIngredientInWriteOff(ingredientInWriteOff);
        }
    }

    private void decreaseIngredients(Map<String, String> ingredients){
        for (Map.Entry<String, String> entry : ingredients.entrySet()){
            ingredientService.decreaseIngredient(entry.getKey(), Float.parseFloat(entry.getValue()));
        }
    }

    private void decreaseItems(Map<String, String> items){
        for(Map.Entry<String, String> entry : items.entrySet()){
            itemService.decreaseItem(entry.getKey(),Float.parseFloat(entry.getValue()));
        }
    }


    private WriteOff checkIfExists(int id) {
        return writeOffRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Write-off", "id", Integer.toString(id))
        );
    }

    @Override
    public GoodsWrapperForWriteOff checkGoods(Map<String, String> goods, IngredientService ingredientService, ItemService itemService) throws ExecutionException, InterruptedException {
        return ContainGoods.super.checkGoods(goods, ingredientService, itemService);
    }

    private WriteOffDto convertToWriteOffDto(WriteOff writeOff){
        return WriteOffMapper.mapToWriteOffDto(writeOff);
    }

    private WriteOff convertToWriteOff(AddWriteOffDto addWriteOffDto){
        return modelMapper.map(addWriteOffDto, WriteOff.class);
    }
}
