package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.dto.ErrorResponseDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto.TotalFinanceResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.TotalFinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/totalFinance")
@AllArgsConstructor
@Tag(
        name = "CRUD REST APIs for total finance info in Coffee App",
        description = "CRUD REST APIs in Coffee App to FETCH total finance info"
)
public class TotalFinanceController {

    private final TotalFinanceService totalFinanceService;

    @Operation(
            summary = "Fetch total finance info  REST API",
            description = "REST API to fetch total finance info"
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
    public TotalFinanceResponse getTotalFinance(){
        return totalFinanceService.get();
    }
}
