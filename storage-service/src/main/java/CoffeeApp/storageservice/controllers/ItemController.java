package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.ItemConstants;
import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.itemService.ItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ItemResponse getItems(){
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Integer id){
        ItemDto itemDto = itemService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(itemDto);
    }

    @GetMapping("/get/{name}")
    public ItemResponse getItemByName(@PathVariable("name") String name){
        return itemService.findItemsByName(name);
    }


    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addItem(@Valid @RequestBody AddItemDto addItemDto){
        itemService.addItem(addItemDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ItemConstants.STATUS_201, ItemConstants.MESSAGE_201));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseDto> updateItem(@PathVariable("id") Integer id, @Valid @RequestBody AddItemDto addItemDto){
        boolean isUpdated = itemService.updateItem(id, addItemDto);
        if (isUpdated){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ItemConstants.STATUS_417, ItemConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto> deleteItem(@PathVariable("id") Integer id){
        boolean isDeleted = itemService.deleteItem(id);
        if (isDeleted){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ItemConstants.STATUS_417, ItemConstants.MESSAGE_417_DELETE));
        }
    }


    private ResponseEntity<ResponseDto> responseStatusOk(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(ItemConstants.STATUS_200, ItemConstants.MESSAGE_200));
    }
}
