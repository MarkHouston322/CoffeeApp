package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.ItemInFridgeConstants;
import CoffeeApp.storageservice.dto.ErrorResponseDto;
import CoffeeApp.storageservice.dto.itemDto.AddItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
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
@RequestMapping("/fridge")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Items in fridge in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, FETCH AND DELETE items in fridge details"
)
public class FridgeController {

    private final ItemInFridgeService itemInFridgeService;

    @Operation(
            summary = "Fetch Items in fridge REST API",
            description = "REST API to fetch Items which now are in fridge"
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
    public ItemInFridgeResponse getItemsInFridge() {
        return itemInFridgeService.getItemsFromFridge();
    }

    @Operation(
            summary = "Fetch all Items in fridge REST API",
            description = "REST API to fetch all Items which in fridge"
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
    @GetMapping("/history")
    public ItemInFridgeResponse getItemsInFridgeHistory() {
        return itemInFridgeService.findAll();
    }

    @Operation(
            summary = "Fetch Item in fridge REST API",
            description = "REST API to fetch Item history being in fridge"
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
    @GetMapping("/item/{name}")
    public ItemInFridgeResponse getItemHistoryInFridge(@PathVariable("name") String name) {
        return itemInFridgeService.findByItemName(name);
    }

    @Operation(
            summary = "Add Item in fridge REST API",
            description = "REST API to add new Item in fridge inside Coffee App"
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
    public ResponseEntity<ResponseDto> addItemToFridge(@Valid @RequestBody AddItemInFridgeDto item) {
        itemInFridgeService.addItemToFridge(item);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ItemInFridgeConstants.STATUS_201, ItemInFridgeConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Item in fridge Details REST API",
            description = "REST API to remove Item from fridge based on an id"
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
    @PatchMapping("/remove/{id}")
    public ResponseEntity<ResponseDto> removeItemFromFridge(@PathVariable("id") Integer id) {
        itemInFridgeService.removeItemFromFridge(id);
        return responseStatusOk();
    }


    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(ItemInFridgeConstants.STATUS_200, ItemInFridgeConstants.MESSAGE_200));
    }
}
