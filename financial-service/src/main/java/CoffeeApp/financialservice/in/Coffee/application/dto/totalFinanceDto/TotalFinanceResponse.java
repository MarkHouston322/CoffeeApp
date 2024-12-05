package CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TotalFinanceResponse {

    private List<TotalFinanceDto> totalFinance;
}
