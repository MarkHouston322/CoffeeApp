package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.AcceptanceConstants;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceResponse;
import CoffeeApp.storageservice.dto.acceptanceDto.AddAcceptanceDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.services.AcceptanceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/acceptances")
@AllArgsConstructor
public class AcceptanceController {

    private final AcceptanceService acceptanceService;

    @GetMapping
    public AcceptanceResponse findAll(){
        return acceptanceService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcceptanceDto> findById(@PathVariable("id") Integer id){
        AcceptanceDto acceptanceDto = acceptanceService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(acceptanceDto);
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<IngredientInDto>> getIngredientsInAcceptance(@PathVariable("id") Integer acceptanceId){
        List<IngredientInDto> ingredients = acceptanceService.getIngredientByAcceptanceId(acceptanceId);
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemInDto>> getItemsInAcceptance(@PathVariable("id") Integer acceptanceId){
        List<ItemInDto> items = acceptanceService.getItemsByAcceptanceId(acceptanceId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> makeAcceptance(@Valid @RequestBody AddAcceptanceDto acceptanceDto,
                                                      @RequestParam Map<String, String> ingredients) throws ExecutionException, InterruptedException {
        acceptanceService.addAcceptance(acceptanceDto,ingredients);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AcceptanceConstants.STATUS_201, AcceptanceConstants.MESSAGE_201));
    }

    @PostMapping("/add/file/{surchargeRatio}")
    public ResponseEntity<ResponseDto> makeAcceptanceFromFile(@RequestParam("file") MultipartFile file,
                                                              @RequestParam Map<String, String> isIngredient,
                                                              @PathVariable("surchargeRatio") String surchargeRatio){
        acceptanceService.addAcceptanceFromFileV2(file,isIngredient, surchargeRatio);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AcceptanceConstants.STATUS_201, AcceptanceConstants.MESSAGE_201));
    }
}
