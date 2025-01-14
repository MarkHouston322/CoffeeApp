package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.IngredientConstants;
import CoffeeApp.storageservice.dto.ErrorResponseDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
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

@RestController
@RequestMapping("/ingredients")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Ingredients in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE ingredients details"
)
public class IngredientController {

    private final IngredientService ingredientService;

    @Operation(
            summary = "Fetch  all Ingredients REST API",
            description = "REST API to fetch all Ingredients"
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
    public IngredientResponse getIngredients() {
        return ingredientService.findAll();
    }

    @Operation(
            summary = "Fetch Ingredient Details REST API",
            description = "REST API to fetch Ingredient details based on an id"
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
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable("id") Integer id) {
        IngredientDto ingredientDto = ingredientService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ingredientDto);
    }

    @Operation(
            summary = "Fetch Ingredients Details REST API",
            description = "REST API to fetch Ingredients details based on a name"
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
    public IngredientResponse getIngredientsByName(@PathVariable("name") String name) {
        return ingredientService.findIngredientsByName(name);
    }

    @Operation(
            summary = "Add Ingredient REST API",
            description = "REST API to add new Ingredient inside Coffee App"
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
    public ResponseEntity<ResponseDto> addIngredient(@Valid @RequestBody IngredientDto ingredientDto) {
        ingredientService.addIngredient(ingredientDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(IngredientConstants.STATUS_201, IngredientConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Ingredient Details REST API",
            description = "REST API to update Ingredient details based on an id"
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
    public ResponseEntity<ResponseDto> updateIngredient(@PathVariable("id") Integer id, @Valid @RequestBody IngredientDto ingredientDto) {
        ingredientService.updateIngredient(id, ingredientDto);
        return responseStatusOk();
    }

    @Operation(
            summary = "Delete Ingredient Details REST API",
            description = "REST API to delete Ingredient details based on an id"
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
