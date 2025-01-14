package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto.TotalFinanceDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto.TotalFinanceResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.models.TotalFinance;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TotalFinanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotalFinanceServiceTest {

    @Mock
    private TotalFinanceRepository totalFinanceRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TotalFinanceService totalFinanceService;

    @Test
    void shouldReturnTotalFinanceDto(){
        TotalFinance totalFinance = new TotalFinance(1000,1000);
        List<TotalFinance> totalFinanceList = List.of(totalFinance);
        TotalFinanceDto totalFinanceDto = new TotalFinanceDto(1000,1000);
        when(totalFinanceRepository.findAll()).thenReturn(totalFinanceList);
        when(modelMapper.map(totalFinance,TotalFinanceDto.class)).thenReturn(totalFinanceDto);
        // when
        TotalFinanceResponse response = totalFinanceService.get();
        // then
        assertThat(response.getTotalFinance()).containsExactly(totalFinanceDto);
        verify(totalFinanceRepository).findAll();
    }

    @Test
    void shouldNotReturnTotalFinanceDto(){
        // given
        when(totalFinanceRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        TotalFinanceResponse response = totalFinanceService.get();
        // then
        assertThat(response.getTotalFinance()).isEmpty();
    }

    @Test
    void shouldInsertCash(){
        // given
        int cashSum = 1000;
        int id = 1;
        TotalFinance totalFinance = new TotalFinance(1000,1000);
        totalFinance.setId(1);
        when(totalFinanceRepository.findById(id)).thenReturn(Optional.of(totalFinance));
        // when
        totalFinanceService.insertCash(cashSum);
        // then
        assertThat(totalFinance.getCash()).isEqualTo(2000);
        verify(totalFinanceRepository).findById(id);
    }

    @Test
    void shouldNotInsertCash(){
        // given
        int id = 1;
        when(totalFinanceRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> totalFinanceService.insertCash(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Total finances")
                .hasMessageContaining("id")
                .hasMessageContaining(Integer.toString(id));
        verify(totalFinanceRepository).findById(id);
    }

    @Test
    void shouldInsertCardPayment(){
        // given
        int cardSum = 1000;
        int id = 1;
        TotalFinance totalFinance = new TotalFinance(1000,1000);
        totalFinance.setId(1);
        when(totalFinanceRepository.findById(id)).thenReturn(Optional.of(totalFinance));
        // when
        totalFinanceService.insertCard(cardSum);
        // then
        assertThat(totalFinance.getCard()).isEqualTo(2000);
        verify(totalFinanceRepository).findById(id);
    }

    @Test
    void shouldNotInsertCardPayment(){
        // given
        int id = 1;
        when(totalFinanceRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> totalFinanceService.insertCard(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Total finances")
                .hasMessageContaining("id")
                .hasMessageContaining(Integer.toString(id));
        verify(totalFinanceRepository).findById(id);
    }

}