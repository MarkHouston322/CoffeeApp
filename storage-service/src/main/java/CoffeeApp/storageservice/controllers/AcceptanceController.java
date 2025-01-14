package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.AcceptanceConstants;
import CoffeeApp.storageservice.dto.ErrorResponseDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceResponse;
import CoffeeApp.storageservice.dto.acceptanceDto.AddAcceptanceDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.services.AcceptanceService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/acceptances")
@AllArgsConstructor
@Tag(
        name = "CRUD REST APIs for Acceptances in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE and FETCH acceptance details"
)
public class AcceptanceController {

    private final AcceptanceService acceptanceService;

    @Operation(
            summary = "Fetch  all Acceptances REST API",
            description = "REST API to fetch all Acceptances"
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
    public AcceptanceResponse findAll(){
        return acceptanceService.findAll();
    }

    @Operation(
            summary = "Fetch Acceptance Details REST API",
            description = "REST API to fetch Acceptance details based on an id"
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
    public ResponseEntity<AcceptanceDto> findById(@PathVariable("id") Integer id){
        AcceptanceDto acceptanceDto = acceptanceService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(acceptanceDto);
    }

    @Operation(
            summary = "Fetch Ingredients Details REST API",
            description = "REST API to fetch Ingredients in Acceptance details based on acceptance id"
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
    public ResponseEntity<List<IngredientInDto>> getIngredientsInAcceptance(@PathVariable("id") Integer acceptanceId){
        List<IngredientInDto> ingredients = acceptanceService.getIngredientByAcceptanceId(acceptanceId);
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    @Operation(
            summary = "Fetch Items Details REST API",
            description = "REST API to fetch Items in Acceptance details based on acceptance id"
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
    public ResponseEntity<List<ItemInDto>> getItemsInAcceptance(@PathVariable("id") Integer acceptanceId){
        List<ItemInDto> items = acceptanceService.getItemsByAcceptanceId(acceptanceId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @Operation(
            summary = "Add Acceptance REST API",
            description = "REST API to add new Acceptance inside Coffee App"
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
    public ResponseEntity<ResponseDto> makeAcceptance(@Valid @RequestBody AddAcceptanceDto acceptanceDto,
                                                      @RequestParam Map<String, String> ingredients) throws ExecutionException, InterruptedException {
        acceptanceService.addAcceptance(acceptanceDto,ingredients);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AcceptanceConstants.STATUS_201, AcceptanceConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Add Acceptance from file REST API",
            description = "REST API to add new Acceptance from file inside Coffee App"
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
    @PostMapping("/add/file/{surchargeRatio}")
    public ResponseEntity<ResponseDto> makeAcceptanceFromFile(@RequestParam("file") MultipartFile file,
                                                              @RequestParam Map<String, String> isIngredient,
                                                              @PathVariable("surchargeRatio") String surchargeRatio){
        acceptanceService.addAcceptanceFromFile(file,isIngredient, surchargeRatio);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AcceptanceConstants.STATUS_201, AcceptanceConstants.MESSAGE_201));
    }
}
