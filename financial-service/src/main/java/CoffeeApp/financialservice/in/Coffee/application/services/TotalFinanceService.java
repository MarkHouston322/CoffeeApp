package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto.TotalFinanceDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto.TotalFinanceResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.models.TotalFinance;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TotalFinanceRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class TotalFinanceService {

    private final TotalFinanceRepository totalFinanceRepository;
    private final ModelMapper modelMapper;
    private final Integer TOTAL_FINANCE_ID = 1;

    public TotalFinanceResponse get(){
        return new TotalFinanceResponse(totalFinanceRepository.findAll().stream().map(this::convertToTotalFinanceDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void insertCash(Integer sum){
        TotalFinance totalFinance = checkIfExists(TOTAL_FINANCE_ID);
        totalFinance.setCash(totalFinance.getCash() + sum);
    }

    @Transactional
    public void insertCard(Integer sum){
        TotalFinance totalFinance = checkIfExists(TOTAL_FINANCE_ID);
        totalFinance.setCard(totalFinance.getCard() + sum);
    }

    private TotalFinance checkIfExists(int id) {
        return totalFinanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Total finances", "id", Integer.toString(id))
        );
    }
    private TotalFinanceDto convertToTotalFinanceDto(TotalFinance totalFinance){
        return modelMapper.map(totalFinance, TotalFinanceDto.class);
    }
}
