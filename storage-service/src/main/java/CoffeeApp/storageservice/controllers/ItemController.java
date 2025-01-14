package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.ItemConstants;
import CoffeeApp.storageservice.dto.ErrorResponseDto;
import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.itemService.ItemService;
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
@RequestMapping("/items")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Items in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE item details"
)
public class ItemController {

    private final ItemService itemService;

    @Operation(
            summary = "Fetch  all Items REST API",
            description = "REST API to fetch all Items"
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
    public ItemResponse getItems() {
        return itemService.findAll();
    }

    @Operation(
            summary = "Fetch Item Details REST API",
            description = "REST API to fetch Item details based on an id"
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
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Integer id) {
        ItemDto itemDto = itemService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(itemDto);
    }

    @Operation(
            summary = "Fetch Items Details REST API",
            description = "REST API to fetch Items details based on a name"
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
    public ItemResponse getItemByName(@PathVariable("name") String name) {
        return itemService.findItemsByName(name);
    }


    @Operation(
            summary = "Add Item REST API",
            description = "REST API to add new Item inside Coffee App"
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
    public ResponseEntity<ResponseDto> addItem(@Valid @RequestBody AddItemDto addItemDto) {
        itemService.addItem(addItemDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ItemConstants.STATUS_201, ItemConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Item Details REST API",
            description = "REST API to update Item details based on an id"
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
    public ResponseEntity<ResponseDto> updateItem(@PathVariable("id") Integer id, @Valid @RequestBody AddItemDto addItemDto) {
        itemService.updateItem(id, addItemDto);
        return responseStatusOk();
    }

    @Operation(
            summary = "Delete Item Details REST API",
            description = "REST API to delete Item details based on an id"
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
    public ResponseEntity<ResponseDto> deleteItem(@PathVariable("id") Integer id) {
        itemService.deleteItem(id);
        return responseStatusOk();
    }


    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(ItemConstants.STATUS_200, ItemConstants.MESSAGE_200));
    }
}
