package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.ItemInFridgeConstants;
import CoffeeApp.storageservice.dto.itemDto.AddItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fridge")
@AllArgsConstructor
@Validated
public class FridgeController {

    private final ItemInFridgeService itemInFridgeService;

    @GetMapping
    public ItemInFridgeResponse getItemsInFridge(){
        return itemInFridgeService.getItemsFromFridge();
    }

    @GetMapping("/history")
    public ItemInFridgeResponse getItemsInFridgeHistory(){
        return itemInFridgeService.findAll();
    }

    @GetMapping("/item")
    public ItemInFridgeResponse getItemHistoryInFridge(@RequestBody Item item){
        return itemInFridgeService.findByItem(item);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addItemToFridge(@Valid @RequestBody AddItemInFridgeDto item){
        itemInFridgeService.addItemToFridge(item);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ItemInFridgeConstants.STATUS_201, ItemInFridgeConstants.MESSAGE_201));
    }

    @PatchMapping("/remove/{id}")
    public ResponseEntity<ResponseDto> removeItemFromFridge(@PathVariable("id") Integer id){
        boolean isRemoved = itemInFridgeService.removeItemFromFridge(id);
        if (isRemoved){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ItemInFridgeConstants.STATUS_417, ItemInFridgeConstants.MESSAGE_417_WRITE_OFF));
        }
    }



    private ResponseEntity<ResponseDto> responseStatusOk(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(ItemInFridgeConstants.STATUS_200, ItemInFridgeConstants.MESSAGE_200));
    }
}
