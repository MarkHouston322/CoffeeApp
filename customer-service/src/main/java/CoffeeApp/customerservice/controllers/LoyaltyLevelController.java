package CoffeeApp.customerservice.controllers;

import CoffeeApp.customerservice.constants.LoyaltyLevelConstants;
import CoffeeApp.customerservice.dto.ErrorResponseDto;
import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelDto;
import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelResponse;
import CoffeeApp.customerservice.dto.ResponseDto;
import CoffeeApp.customerservice.services.LoyaltyLevelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/loyalty")
@AllArgsConstructor
@Validated
public class LoyaltyLevelController {

    private final LoyaltyLevelService loyaltyLevelService;

    @Operation(
            summary = "Fetch  all Loyalty levels REST API",
            description = "REST API to fetch all Loyalty levels"
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
    public LoyaltyLevelResponse findAll(){
        return loyaltyLevelService.findAll();
    }

    @Operation(
            summary = "Fetch Loyalty level details REST API",
            description = "REST API to fetch Loyalty level details based on an id"
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
    public ResponseEntity<LoyaltyLevelDto> getLoyaltyLevel(@PathVariable("id") Integer id){
        LoyaltyLevelDto loyaltyLevelDto = loyaltyLevelService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(loyaltyLevelDto);
    }

    @Operation(
            summary = "Fetch Loyalty level details REST API",
            description = "REST API to fetch Loyalty level details based on a name"
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
    public LoyaltyLevelResponse getLoyaltyLevelsByName(@PathVariable("name") String name){
        return loyaltyLevelService.findByName(name);
    }

    @Operation(
            summary = "Add Loyalty level REST API",
            description = "REST API to add new Loyalty level inside Coffee App"
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
    public ResponseEntity<ResponseDto> addLoyaltyLevel(@Valid @RequestBody LoyaltyLevelDto loyaltyLevelDto){
        loyaltyLevelService.addLoyaltyLevel(loyaltyLevelDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(LoyaltyLevelConstants.STATUS_201, LoyaltyLevelConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Loyalty level Details REST API",
            description = "REST API to update Loyalty level details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
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
    public ResponseEntity<ResponseDto> updateLoyaltyLevel(@PathVariable("id") Integer id,
                                                          @Valid @RequestBody LoyaltyLevelDto loyaltyLevelDto){
        boolean isUpdated = loyaltyLevelService.updateLoyaltyService(id, loyaltyLevelDto);
        if (isUpdated){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoyaltyLevelConstants.STATUS_417, LoyaltyLevelConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Loyalty level REST API",
            description = "REST API to delete Loyalty level details based on an id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
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
    public ResponseEntity<ResponseDto> deleteLoyaltyLevel(@PathVariable("id") Integer id){
        boolean isDeleted = loyaltyLevelService.deleteLoyaltyLevel(id);
        if (isDeleted){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoyaltyLevelConstants.STATUS_417, LoyaltyLevelConstants.MESSAGE_417_DELETE));
        }
    }

    private ResponseEntity<ResponseDto> responseStatusOk(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(LoyaltyLevelConstants.STATUS_200, LoyaltyLevelConstants.MESSAGE_200));
    }
}
