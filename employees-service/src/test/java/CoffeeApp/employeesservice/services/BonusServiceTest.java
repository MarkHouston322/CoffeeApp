package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.bonusDto.BonusDto;
import CoffeeApp.employeesservice.dto.bonusDto.BonusResponse;
import CoffeeApp.employeesservice.exceptions.BonusAlreadyExistsException;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Bonus;
import CoffeeApp.employeesservice.repositories.BonusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BonusServiceTest {

    @Mock
    private BonusRepository bonusRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BonusService bonusService;

    @Test
    void shouldReturnBonusDtoById(){
        // given
        int bonusId = 1;
        Bonus bonus = new Bonus(1000,500);
        bonus.setId(bonusId);

        BonusDto bonusDto = new BonusDto(1000,500);
        when(bonusRepository.findById(bonusId)).thenReturn(Optional.of(bonus));
        when(modelMapper.map(bonus,BonusDto.class)).thenReturn(bonusDto);
        // when
        BonusDto result = bonusService.findById(bonusId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getEdge()).isEqualTo(bonus.getEdge());
        verify(bonusRepository).findById(bonusId);
    }

    @Test
    void shouldNotReturnBonusDtoByIdAndThrowException(){
        // given
        int bonusId = 1;
        when(bonusRepository.findById(bonusId)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> bonusService.findById(bonusId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Bonus", "id", Integer.toString(bonusId));
        verify(bonusRepository).findById(bonusId);
    }

    @Test
    void shouldReturnBonusDtoByEdge(){
        // given
        int edge = 1000;
        Bonus bonus = new Bonus(edge,500);
        BonusDto bonusDto = new BonusDto(edge,500);
        when(bonusRepository.findByEdge(edge)).thenReturn(Optional.of(bonus));
        when(modelMapper.map(bonus,BonusDto.class)).thenReturn(bonusDto);
        // when
        BonusDto result = bonusService.findByEdge(edge);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getEdge()).isEqualTo(bonus.getEdge());
        verify(bonusRepository).findByEdge(edge);
    }

    @Test
    void shouldNotReturnBonusDtoByEdgeAndThrowException(){
        // given
        int edge = 1000;
        when(bonusRepository.findByEdge(edge)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> bonusService.findByEdge(edge))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Bonus", "edge", Integer.toString(edge));
        verify(bonusRepository).findByEdge(edge);
    }

    @Test
    void shouldReturnAllBonuses(){
        // given
        Bonus bonus1 = new Bonus(1000,500);
        Bonus bonus2 = new Bonus(2000,1000);
        List<Bonus> bonuses = Arrays.asList(bonus1,bonus2);

        BonusDto bonusDto1 = new BonusDto(1000,500);
        BonusDto bonusDto2 = new BonusDto(2000,1000);
        when(bonusRepository.findAll()).thenReturn(bonuses);
        when(modelMapper.map(bonus1,BonusDto.class)).thenReturn(bonusDto1);
        when(modelMapper.map(bonus2,BonusDto.class)).thenReturn(bonusDto2);
        // when
        BonusResponse response = bonusService.findAll();
        // then
        assertThat(response.getBonuses()).containsExactly(bonusDto1,bonusDto2);
        verify(bonusRepository).findAll();
    }

    @Test
    void shouldNotReturnAllBonuses(){
        // given
        when(bonusRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        BonusResponse response = bonusService.findAll();
        // then
        assertThat(response.getBonuses()).isEmpty();
        verify(bonusRepository).findAll();
    }

    @Test
    void shouldAddBonus(){
        // given
        BonusDto bonusDto = new BonusDto(1000,500);
        when(bonusRepository.findByEdge(bonusDto.getEdge())).thenReturn(Optional.empty());
        setModelMapper(bonusDto);
        // when
        bonusService.addBonus(bonusDto);
        // then
        ArgumentCaptor<Bonus> argumentCaptor = ArgumentCaptor.forClass(Bonus.class);
        verify(bonusRepository).save(argumentCaptor.capture());
        Bonus bonus = argumentCaptor.getValue();
        assertThat(bonus.getEdge()).isEqualTo(bonusDto.getEdge());
        assertThat(bonus.getValue()).isEqualTo(bonusDto.getValue());
    }

    @Test
    void shouldNotAddBonus(){
        // given
        BonusDto bonusDto = new BonusDto(1000,500);
        Bonus existingBonus = new Bonus();
        existingBonus.setEdge(bonusDto.getEdge());
        when(bonusRepository.findByEdge(bonusDto.getEdge())).thenReturn(Optional.of(existingBonus));
        setModelMapper(bonusDto);
        // when
        assertThatThrownBy(() -> bonusService.addBonus(bonusDto))
                .isInstanceOf(BonusAlreadyExistsException.class)
                .hasMessageContaining("Bonus has already been added with this edge");
    }

    @Test
    void shouldDeleteBonusById(){
        // given
        int bonusId = 1;
        Bonus bonus = new Bonus();
        bonus.setId(bonusId);

        when(bonusRepository.findById(bonusId)).thenReturn(Optional.of(bonus));
        doNothing().when(bonusRepository).deleteById(bonusId);
        // when
        boolean result = bonusService.deleteBonus(bonusId);
        // then
        assertThat(result).isTrue();
        verify(bonusRepository).findById(bonusId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(bonusRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(bonusId);
    }

    @Test
    void shouldUpdateBonusById(){
        // given
        int bonusId = 1;
        BonusDto bonusDto = new BonusDto(1000, 500);
        Bonus existingBonus = new Bonus(1500,750);
        existingBonus.setId(bonusId);

        when(bonusRepository.findById(bonusId)).thenReturn(Optional.of(existingBonus));
        setModelMapper(bonusDto);
        // when
        boolean isUpdated = bonusService.updateBonus(bonusId,bonusDto);
        // then
        assertThat(isUpdated).isTrue();
        ArgumentCaptor<Bonus> bonusCaptor = ArgumentCaptor.forClass(Bonus.class);
        verify(bonusRepository).save(bonusCaptor.capture());
        Bonus updatedBonus = bonusCaptor.getValue();
        assertThat(updatedBonus.getEdge()).isEqualTo(bonusDto.getEdge());
        assertThat(updatedBonus.getValue()).isEqualTo(bonusDto.getValue());
    }

    private void setModelMapper(BonusDto bonusDto) {
        when(modelMapper.map(bonusDto, Bonus.class)).thenReturn(new Bonus(bonusDto.getEdge(), bonusDto.getValue()));
    }
  
}