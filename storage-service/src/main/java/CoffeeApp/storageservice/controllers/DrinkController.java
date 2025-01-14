package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.DrinkConstants;
import CoffeeApp.storageservice.dto.drinkDto.AddDrinkDto;
import CoffeeApp.storageservice.dto.drinkDto.DrinkResponse;
import CoffeeApp.storageservice.dto.drinkDto.ShowDrinkDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.DrinkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/drinks")
@AllArgsConstructor
@Validated
public class DrinkController {

    private final DrinkService drinkService;

    @GetMapping
    public DrinkResponse getDrinks() {
        return drinkService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowDrinkDto> getDrink(@PathVariable("id") Integer id) {
        ShowDrinkDto showDrinkDto = drinkService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(showDrinkDto);
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<IngredientInDto>> getIngredientsForDrink(@PathVariable("id") Integer drinkId) {
        List<IngredientInDto> ingredients = drinkService.getIngredientsByDrinkId(drinkId);
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    @GetMapping("/get/{name}")
    public DrinkResponse getDrinksByName(@PathVariable("name") String name) {
        return drinkService.findDrinksByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addDrink(@Valid @RequestBody AddDrinkDto addDrinkDto,
                                                @RequestParam Map<String, String> ingredients) throws ExecutionException, InterruptedException {
        drinkService.addDrink(addDrinkDto, ingredients);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(DrinkConstants.STATUS_201, DrinkConstants.MESSAGE_201));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseDto> updateDrink(@PathVariable("id") Integer id,
                                                   @Valid @RequestBody AddDrinkDto addDrinkDto,
                                                   @RequestParam Map<String, String> ingredients) {
        boolean isUpdated = drinkService.updateDrink(id, addDrinkDto, ingredients);
        if (isUpdated) {
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(DrinkConstants.STATUS_417, DrinkConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto> deleteDrink(@PathVariable("id") Integer id) {
        drinkService.deleteDrinkById(id);
        return responseStatusOk();
    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(DrinkConstants.STATUS_200, DrinkConstants.MESSAGE_200));
    }

}
