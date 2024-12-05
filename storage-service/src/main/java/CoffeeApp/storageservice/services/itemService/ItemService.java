package CoffeeApp.storageservice.services.itemService;

import CoffeeApp.storageservice.dto.itemDto.*;
import CoffeeApp.storageservice.dto.messages.GoodMessage;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.ItemAlreadyExistsException;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.repositories.itemRepository.ItemRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final StreamBridge streamBridge;

    public ItemDto findById(int id){
        Item item = checkIfExists(id);
        return convertToItemDto(item);
    }

    @Transactional(noRollbackFor = ResourceNotFoundException.class)
    public Item findByName(String name){
        return itemRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Item", "name", name)
        );
    }

    public ItemResponse findItemsByName(String name){
        return new ItemResponse(itemRepository.findByNameStartingWith(name).stream().map(this::convertToItemDto)
                .collect(Collectors.toList()));
    }


    public ItemResponse findAll(){
        return new ItemResponse(itemRepository.findAll().stream().map(this::convertToItemDto).collect(Collectors.toList()));
    }

    @Transactional
    public void addItem(AddItemDto addItemDto){
        Item itemToAdd = convertToItem(addItemDto);
        Optional<Item> optionalItem = itemRepository.findByName(itemToAdd.getName());
        if (optionalItem.isPresent()){
            throw new ItemAlreadyExistsException("Item has already been added with this name: " + itemToAdd.getName());
        }
        itemToAdd.setPrice((int) Math.ceil((itemToAdd.getCostPrice() * itemToAdd.getSurchargeRatio()) / 10) * 10);
        itemToAdd.setQuantityInStock(itemToAdd.getQuantityInStock());
        itemRepository.save(itemToAdd);
        sendItem(itemToAdd);
    }

    @Transactional
    public boolean deleteItem(int id){
        checkIfExists(id);
        itemRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean updateItem(int id, AddItemDto addItemDto){
        boolean isUpdated = false;
        if (addItemDto != null){
            Item updatedItem = convertToItem(addItemDto);
            updatedItem.setId(id);
            itemRepository.save(updatedItem);
            sendItem(updatedItem);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Transactional
    public void increaseItem(String name, Float increaseQuantity){
        itemRepository.findByName(name).ifPresent(
                item -> item.setQuantityInStock(item.getQuantityInStock() + increaseQuantity)
        );
    }

    @Transactional
    public void decreaseItem(String name, Float decreaseQuantity){
        itemRepository.findByName(name).ifPresent(
                item -> item.setQuantityInStock(item.getQuantityInStock() - decreaseQuantity)
        );
    }


    private Item checkIfExists(int id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item", "id", Integer.toString(id))
        );
    }

    public float calculateCost(Map<String, String> items) {
        float total = 0;
        for (Map.Entry<String, String> entry : items.entrySet()) {
            Item item = findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            float itemCost = item.getCostPrice() * quantity;
            total += itemCost;

        }

        return total;
    }

    public float calculateCostFromFile(List<AddItemDto> items){
        float total = 0;
        for (AddItemDto addItemDto : items){
            float itemCost = addItemDto.getCostPrice() * addItemDto.getQuantityInStock();
            total += itemCost;
        }
        return total;
    }

    private void sendItem(Item item){
        GoodMessage itemMessage = new GoodMessage(item.getName(),item.getPrice(), "item");
        streamBridge.send("sendGood-out-0",itemMessage);
    }

    private ItemDto convertToItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    private Item convertToItem(AddItemDto addItemDto) {
        return modelMapper.map(addItemDto, Item.class);
    }

    private GoodMessage convertToGoodMessage(Item item){
        return modelMapper.map(item, GoodMessage.class);
    }

}
