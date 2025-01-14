package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.IngredientConstants;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
@Validated
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public IngredientResponse getIngredients() {
        return ingredientService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable("id") Integer id) {
        IngredientDto ingredientDto = ingredientService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ingredientDto);
    }

    @GetMapping("/get/{name}")
    public IngredientResponse getIngredientsByName(@PathVariable("name") String name) {
        return ingredientService.findIngredientsByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addIngredient(@Valid @RequestBody IngredientDto ingredientDto) {
        ingredientService.addIngredient(ingredientDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(IngredientConstants.STATUS_201, IngredientConstants.MESSAGE_201));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseDto> updateIngredient(@PathVariable("id") Integer id, @Valid @RequestBody IngredientDto ingredientDto) {
        ingredientService.updateIngredient(id, ingredientDto);
        return responseStatusOk();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto> deleteIngredient(@PathVariable("id") Integer id) {
        ingredientService.deleteIngredient(id);
        return responseStatusOk();
    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(IngredientConstants.STATUS_200, IngredientConstants.MESSAGE_200));
    }
}
