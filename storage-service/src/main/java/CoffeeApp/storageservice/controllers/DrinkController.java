package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.DrinkConstants;
import CoffeeApp.storageservice.dto.ErrorResponseDto;
import CoffeeApp.storageservice.dto.drinkDto.AddDrinkDto;
import CoffeeApp.storageservice.dto.drinkDto.DrinkResponse;
import CoffeeApp.storageservice.dto.drinkDto.ShowDrinkDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.DrinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "CRUD REST APIs for Drinks in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE drink details"
)
public class DrinkController {

    private final DrinkService drinkService;

    @Operation(
            summary = "Fetch  all Drinks REST API",
            description = "REST API to fetch all Drinks"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping
    public DrinkResponse getDrinks() {
        return drinkService.findAll();
    }

    @Operation(
            summary = "Fetch Drink Details REST API",
            description = "REST API to fetch Drink details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ShowDrinkDto> getDrink(@PathVariable("id") Integer id) {
        ShowDrinkDto showDrinkDto = drinkService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(showDrinkDto);
    }

    @Operation(
            summary = "Fetch Drink Details REST API",
            description = "REST API to fetch Ingredients in Drink details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<IngredientInDto>> getIngredientsForDrink(@PathVariable("id") Integer drinkId) {
        List<IngredientInDto> ingredients = drinkService.getIngredientsByDrinkId(drinkId);
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    @Operation(
            summary = "Fetch Drink Details REST API",
            description = "REST API to fetch Drink details based on a name"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/get/{name}")
    public DrinkResponse getDrinksByName(@PathVariable("name") String name) {
        return drinkService.findDrinksByName(name);
    }

    @Operation(
            summary = "Add Drink REST API",
            description = "REST API to add new Drink inside Coffee App"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addDrink(@Valid @RequestBody AddDrinkDto addDrinkDto,
                                                @RequestParam Map<String, String> ingredients) throws ExecutionException, InterruptedException {
        drinkService.addDrink(addDrinkDto, ingredients);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(DrinkConstants.STATUS_201, DrinkConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Drink Details REST API",
            description = "REST API to update Drink details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
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

    @Operation(
            summary = "Delete Drink Details REST API",
            description = "REST API to delete Drink details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
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
