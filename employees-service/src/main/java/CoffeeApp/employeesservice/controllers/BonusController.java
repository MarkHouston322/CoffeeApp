package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.constants.BonusConstants;
import CoffeeApp.employeesservice.dto.ErrorResponseDto;
import CoffeeApp.employeesservice.dto.bonusDto.BonusDto;
import CoffeeApp.employeesservice.dto.bonusDto.BonusResponse;
import CoffeeApp.employeesservice.dto.ResponseDto;
import CoffeeApp.employeesservice.models.Bonus;
import CoffeeApp.employeesservice.services.BonusService;
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
@RequestMapping("/bonus")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Salary bonuses in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE salary bonuses"
)
public class BonusController {

    private final BonusService bonusService;

    @Operation(
            summary = "Fetch  all salary Bonuses REST API",
            description = "REST API to fetch all salary Bonuses"
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
    public BonusResponse findAll() {
        return bonusService.findAll();
    }

    @Operation(
            summary = "Fetch salary Bonus Details REST API",
            description = "REST API to fetch salary Bonus details based on an id"
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
    public ResponseEntity<BonusDto> findById(@PathVariable("id") Integer id) {
        BonusDto bonusDto = bonusService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bonusDto);
    }

    @Operation(
            summary = "Fetch salary Bonus Details REST API",
            description = "REST API to fetch salary Bonus details based on an edge"
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
    @GetMapping("/get/{edge}")
    public ResponseEntity<BonusDto> findByEdge(@PathVariable("edge") Integer edge) {
        BonusDto bonusDto = bonusService.findByEdge(edge);
        return ResponseEntity.status(HttpStatus.OK).body(bonusDto);
    }

    @Operation(
            summary = "Add salary Bonus REST API",
            description = "REST API to add new salary Bonus inside Coffee App"
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
    public ResponseEntity<ResponseDto> addBonus(@Valid @RequestBody BonusDto bonusDto) {
        bonusService.addBonus(bonusDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(BonusConstants.STATUS_201, BonusConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update salary Bonus Details REST API",
            description = "REST API to update salary Bonus details based on an id"
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
    public ResponseEntity<ResponseDto> updateBonus(@PathVariable("id") Integer id,
                                                   @Valid @RequestBody BonusDto bonusDto) {
        bonusService.updateBonus(id, bonusDto);
        return responseStatusOk();
    }

    @Operation(
            summary = "Delete salary Bonus REST API",
            description = "REST API to delete salary Bonus based on an id"
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
    public ResponseEntity<ResponseDto> deleteBonus(@PathVariable("id") Integer id) {
        bonusService.deleteBonus(id);
        return responseStatusOk();
    }


    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(BonusConstants.STATUS_200, BonusConstants.MESSAGE_200));
    }
}
