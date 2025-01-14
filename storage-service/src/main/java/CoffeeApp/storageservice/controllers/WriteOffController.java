package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.WriteOffConstants;
import CoffeeApp.storageservice.dto.ErrorResponseDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.dto.writeOffDto.AddWriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffResponse;
import CoffeeApp.storageservice.services.WriteOffService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/write-offs")
@AllArgsConstructor
@Tag(
        name = "CRUD REST APIs for Write-off in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE and FETCH write-off details"
)
public class WriteOffController {

    private final WriteOffService writeOffService;

    @Operation(
            summary = "Fetch  all Write-offs REST API",
            description = "REST API to fetch all Write-offs"
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
    public WriteOffResponse findAll(){
        return writeOffService.findAll();
    }

    @Operation(
            summary = "Fetch Write-off Details REST API",
            description = "REST API to fetch Write-off details based on an id"
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
    public ResponseEntity<WriteOffDto> findById(@PathVariable("id") Integer id){
        WriteOffDto writeOffDto = writeOffService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(writeOffDto);
    }

    @Operation(
            summary = "Fetch Write-off Details REST API",
            description = "REST API to fetch Ingredients in Write-off based on an id"
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
    public ResponseEntity<List<IngredientInDto>> getIngredientsInWriteOff(@PathVariable("id") Integer writeOffId){
        List<IngredientInDto> ingredients = writeOffService.getIngredientsByWriteOffId(writeOffId);
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    @Operation(
            summary = "Fetch Write-off Details REST API",
            description = "REST API to fetch Items in Write-off based on an id"
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
    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemInDto>> getItemsInWriteOff(@PathVariable("id") Integer writeOffId){
        List<ItemInDto> items = writeOffService.getItemsByWriteOffId(writeOffId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @Operation(
            summary = "Add Write-off REST API",
            description = "REST API to add new Write-off inside Coffee App"
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
    public ResponseEntity<ResponseDto> writeOff(@Valid @RequestBody AddWriteOffDto addWriteOffDto,
                                                @RequestParam Map<String, String> goods) throws ExecutionException, InterruptedException {
        writeOffService.addWriteOffGoods(addWriteOffDto, goods);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(WriteOffConstants.STATUS_201, WriteOffConstants.MESSAGE_201));
    }
}
