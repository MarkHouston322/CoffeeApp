package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.constants.SessionConstants;
import CoffeeApp.financialservice.in.Coffee.application.dto.ErrorResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.ResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sessions")
@AllArgsConstructor
@Tag(
        name = "CRUD REST APIs for financial sessions in Coffee App",
        description = "CRUD REST APIs in Coffee App to FETCH financial sessions infos"
)
public class SessionController {

    private final SessionService sessionService;

    @Operation(
            summary = "Fetch  all financial sessions  REST API",
            description = "REST API to fetch all financial sessions"
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
    public SessionResponse findAll() {
        return sessionService.findAll();
    }

    @Operation(
            summary = "Fetch financial session details REST API",
            description = "REST API to fetch financial session details based on an id"
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
    public ResponseEntity<SessionDto> findById(@PathVariable("id") Integer id) {
        SessionDto sessionDto = sessionService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(sessionDto);
    }

    @Operation(
            summary = "Open financial session REST API",
            description = "REST API to open new financial session"
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
    @GetMapping("/open")
    public ResponseEntity<ResponseDto> openSession(@RequestHeader("Preferred-Username") String username) {
        boolean isOpened = sessionService.openSession(username);
        if (isOpened) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto(SessionConstants.STATUS_201, SessionConstants.MESSAGE_201_OPEN));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(SessionConstants.STATUS_417, SessionConstants.MESSAGE_417_OPEN));
        }
    }


    @Operation(
            summary = "Close financial session REST API",
            description = "REST API to close new financial session"
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
    @GetMapping("/close")
    public ResponseEntity<ResponseDto> closeSession() {
        boolean isClosed = sessionService.closeSession();
        if (isClosed) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(SessionConstants.STATUS_200, SessionConstants.MESSAGE_200_CLOSE));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(SessionConstants.STATUS_417, SessionConstants.MESSAGE_417_CLOSE));
        }
    }

}
