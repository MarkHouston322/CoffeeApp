package CoffeeApp.storageservice.services.itemService;

import CoffeeApp.storageservice.dto.itemDto.AddItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeResponse;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInFridge;
import CoffeeApp.storageservice.repositories.itemRepository.ItemInFridgeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemInFridgeService {

    private final ItemInFridgeRepository itemInFridgeRepository;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    public ItemInFridgeDto findById(Integer id){
        ItemInFridge item = checkIfExists(id);
        return convertToItemInFridgeDto(item);
    }

    public ItemInFridgeResponse findAll(){
        return new ItemInFridgeResponse(itemInFridgeRepository.findAll().stream().map(this::convertToItemInFridgeDto)
                .collect(Collectors.toList()));
    }

    public ItemInFridgeResponse findByItem(Item item){
        return new ItemInFridgeResponse(itemInFridgeRepository.findByItem(item).stream().map(this::convertToItemInFridgeDto)
                .collect(Collectors.toList()));
    }

    public ItemInFridgeResponse getItemsFromFridge(){
        return new ItemInFridgeResponse(itemInFridgeRepository.getItemsInFridge().stream().map(this::convertToItemInFridgeDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addItemToFridge(AddItemInFridgeDto addItemInFridgeDto){
        if (addItemInFridgeDto != null){
            ItemInFridge itemToAdd = convertToItemInFridge(addItemInFridgeDto);
            itemToAdd.setStorageTime(itemToAdd.getStorageTime() * 3_600_000);
            itemToAdd.setPlaceDate(new Date());
            itemToAdd.setIsSold(false);
            itemToAdd.setExpired(false);
            itemInFridgeRepository.save(itemToAdd);
        }
    }

    @Transactional
    public boolean removeItemFromFridge(Integer id){
        boolean isRemoved = false;
        Item itemToRemove = checkIfExists(id).getItem();
        ItemInFridge itemInFridge = isItemInFridge(itemToRemove);
        if (itemInFridge != null){
            itemInFridge.setIsSold(true);
            itemInFridge.setSoldDate(LocalDateTime.now());
            isRemoved = true;
        }
        return isRemoved;
    }

    public void checkForExpiration(){
        List<ItemInFridge> items = itemInFridgeRepository.getItemsInFridge();
        for (ItemInFridge item : items){
            long passedTime = Math.abs(new Date().getTime() - item.getPlaceDate().getTime());
            if (passedTime >= item.getStorageTime()){
                item.setExpired(true);
                System.out.println("Item with name: " + item.getItem().getName() + " is expired");
                // заглушка уведомлений
            }
        }
    }

    public void writeOffItemsFromFridge(Map<String, String> items){
        for (Map.Entry<String,String> entry : items.entrySet()){
            removeItemsFromFridgeIfExists(entry.getKey());
        }
    }

    public void removeItemsFromFridgeIfExists (String name){
        int itemId = itemService.findByName(name).getId();
        try {
            int itemInFridgeId = findItemInFridgeIdByItemId(itemId);
            removeItemFromFridge(itemInFridgeId);
        } catch (ResourceNotFoundException ignored){}
    }

    public int findItemInFridgeIdByItemId(Integer itemId){
        Optional<ItemInFridge> itemInFridge = itemInFridgeRepository.findByItemId(itemId);
        if (itemInFridge.isPresent()){
            return  itemInFridge.get().getId();
        } else {
            throw new ResourceNotFoundException("Item in fridge", "name", Integer.toString(itemId));
        }
    }

    private ItemInFridge isItemInFridge(Item item){
        Optional<ItemInFridge> optional = itemInFridgeRepository.isItemInFridge(item);
        return optional.orElse(null);
    }

    private ItemInFridgeDto convertToItemInFridgeDto(ItemInFridge itemInFridge){
        return modelMapper.map(itemInFridge, ItemInFridgeDto.class);
    }

    private ItemInFridge convertToItemInFridge(AddItemInFridgeDto itemInFridgeDto){
        return modelMapper.map(itemInFridgeDto, ItemInFridge.class);
    }

    private ItemInFridge checkIfExists(int id){
        return itemInFridgeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item in fridge", "id", Integer.toString(id))
        );
    }

}
