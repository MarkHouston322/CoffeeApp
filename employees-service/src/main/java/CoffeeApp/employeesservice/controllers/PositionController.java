package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.constants.PositionConstants;
import CoffeeApp.employeesservice.dto.ErrorResponseDto;
import CoffeeApp.employeesservice.dto.positionDto.PositionDto;
import CoffeeApp.employeesservice.dto.positionDto.PositionResponse;
import CoffeeApp.employeesservice.dto.ResponseDto;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.services.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/position")
@AllArgsConstructor
@Validated
@Tag(
        name = "CRUD REST APIs for Positions in Coffee App",
        description = "CRUD REST APIs in Coffee App to CREATE, UPDATE, FETCH AND DELETE positions"
)
public class PositionController {

    private final PositionService positionService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Fetch  all Positions  REST API",
            description = "REST API to fetch all Positions"
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
    public PositionResponse findAll(){
        return positionService.findAll();
    }

    @Operation(
            summary = "Fetch Position Details REST API",
            description = "REST API to fetch Position details based on an id"
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
    public ResponseEntity<PositionDto> getPosition(@PathVariable("id") Integer id){
        PositionDto positionDto = convertToPositionDto(positionService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(positionDto);
    }

    @Operation(
            summary = "Fetch Position Details REST API",
            description = "REST API to fetch Position details based on a name"
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
    public PositionResponse findPositionByName(@PathVariable("name") String name){
        return positionService.findByName(name);
    }

    @Operation(
            summary = "Add Position REST API",
            description = "REST API to add new Position inside Coffee App"
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
    public ResponseEntity<ResponseDto> addPosition(@Valid @RequestBody PositionDto positionDto){
        positionService.addPosition(positionDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(PositionConstants.STATUS_201, PositionConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Update Position Details REST API",
            description = "REST API to update Position details based on an id"
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
    public ResponseEntity<ResponseDto> updatePosition(@PathVariable("id") Integer id,
                                                      @Valid @RequestBody PositionDto positionDto){
        boolean isUpdated = positionService.updatePosition(id,positionDto);
        if (isUpdated){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(PositionConstants.STATUS_417, PositionConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Position REST API",
            description = "REST API to delete Position based on an id"
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
    public ResponseEntity<ResponseDto> deletePosition(@PathVariable("id") Integer id){
        boolean isDeleted = positionService.deletePosition(id);
        if (isDeleted){
            return responseStatusOk();
        } else {
           return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(PositionConstants.STATUS_417, PositionConstants.MESSAGE_417_DELETE));
        }
    }

    private ResponseEntity<ResponseDto> responseStatusOk(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(PositionConstants.STATUS_200, PositionConstants.MESSAGE_200));
    }

    private PositionDto convertToPositionDto(Position position){
        return modelMapper.map(position, PositionDto.class);
    }
}
