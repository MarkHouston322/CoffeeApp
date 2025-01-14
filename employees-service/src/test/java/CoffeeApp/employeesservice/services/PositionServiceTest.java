package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.positionDto.PositionDto;
import CoffeeApp.employeesservice.dto.positionDto.PositionResponse;
import CoffeeApp.employeesservice.exceptions.PositionAlreadyExistsException;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.repositories.PositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PositionService positionService;

    @Test
    void shouldPositionById(){
        // given
        int positionId = 1;
        Position position = new Position("Manager",4000f,0.1f);
        position.setId(positionId);

        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        // when
        Position result = positionService.findById(positionId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(positionId);
        verify(positionRepository).findById(positionId);
    }

    @Test
    void shouldNotPositionById(){
        int positionId = 1;
        when(positionRepository.findById(positionId)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> positionService.findById(positionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Position", "id", Integer.toString(positionId));
        verify(positionRepository).findById(positionId);
    }

    @Test
    void shouldReturnPositionsDtoByNameStartingWith(){
        // given
        String positionName = "Manager";
        Position position1 = new Position(positionName,4000f,0.1f);
        Position position2 = new Position(positionName + " of department",3000f,0.05f);
        List<Position> positions = Arrays.asList(position1,position2);
        PositionDto positionDto1 = new PositionDto(positionName,4000f,0.1f);
        PositionDto positionDto2 =  new PositionDto(positionName + " of department",3000f,0.05f);
        when(positionRepository.findByNameStartingWith(positionName)).thenReturn(positions);
        when(modelMapper.map(position1,PositionDto.class)).thenReturn(positionDto1);
        when(modelMapper.map(position2,PositionDto.class)).thenReturn(positionDto2);
        // when
        PositionResponse response = positionService.findByName(positionName);
        // then
        assertThat(response.getPositions()).containsExactly(positionDto1,positionDto2);
        verify(positionRepository).findByNameStartingWith(positionName);
    }

    @Test
    void shouldNotReturnPositionsDtoByNameStartingWith(){
        // given
        String positionName = "Manager";
        when(positionRepository.findByNameStartingWith(positionName)).thenReturn(new ArrayList<>());
        // when
        PositionResponse response = positionService.findByName(positionName);
        // then
        assertThat(response.getPositions()).isEmpty();
        verify(positionRepository).findByNameStartingWith(positionName);
    }

    @Test
    void shouldReturnAllPositionsDto(){
        // given
        Position position1 = new Position("Manager",4000f,0.1f);
        Position position2 = new Position("Barista",3000f,0.05f);
        List<Position> positions = Arrays.asList(position1,position2);
        PositionDto positionDto1 = new PositionDto("Manager",4000f,0.1f);
        PositionDto positionDto2 =  new PositionDto("Barista",3000f,0.05f);
        when(positionRepository.findAll()).thenReturn(positions);
        when(modelMapper.map(position1,PositionDto.class)).thenReturn(positionDto1);
        when(modelMapper.map(position2,PositionDto.class)).thenReturn(positionDto2);
        // when
        PositionResponse response = positionService.findAll();
        // then
        assertThat(response.getPositions()).containsExactly(positionDto1,positionDto2);
        verify(positionRepository).findAll();
    }

    @Test
    void shouldNotReturnAllPositionsDto(){
        // given
        when(positionRepository.findAll()).thenReturn(new ArrayList<>());
        // when
        PositionResponse response = positionService.findAll();
        // then
        assertThat(response.getPositions()).isEmpty();
        verify(positionRepository).findAll();
    }

    @Test
    void shouldAddPosition(){
        PositionDto positionDto = new PositionDto("Barista",3000f,0.05f);
        when(positionRepository.findByName(positionDto.getName())).thenReturn(Optional.empty());
        setModelMapper(positionDto);
        // when
        positionService.addPosition(positionDto);
        // then
        ArgumentCaptor<Position> positionCaptor = ArgumentCaptor.forClass(Position.class);
        verify(positionRepository).save(positionCaptor.capture());
        Position position = positionCaptor.getValue();
        assertThat(position.getName()).isEqualTo(positionDto.getName());
    }

    @Test
    void shouldNotAddPosition(){
        // given
        PositionDto positionDto = new PositionDto("Barista",3000f,0.05f);
        Position existingPosition = new Position();
        existingPosition.setName(positionDto.getName());
        when(positionRepository.findByName(positionDto.getName())).thenReturn(Optional.of(existingPosition));
        setModelMapper(positionDto);
        // when & then
        assertThatThrownBy(() -> positionService.addPosition(positionDto))
                .isInstanceOf(PositionAlreadyExistsException.class)
                .hasMessageContaining("Position has already been added with this name");

    }

    @Test
    void shouldDeletePositionById(){
        // given
        int positionId =1;
        Position position = new Position();
        position.setId(positionId);
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(position));
        doNothing().when(positionRepository).deleteById(positionId);
        // when
        positionService.deletePosition(positionId);
        // then
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(positionRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(positionId);
        verify(positionRepository).findById(positionId);
    }

    @Test
    void shouldUpdatePositionById(){
        // given
        int positionId = 1;
        PositionDto positionDto = new PositionDto("Barista",4000f,0.05f);
        Position existingPosition = new Position("Barista",3000f,0.05f);
        existingPosition.setId(positionId);
        when(positionRepository.findById(positionId)).thenReturn(Optional.of(existingPosition));
        setModelMapper(positionDto);
        // when
        positionService.updatePosition(positionId,positionDto);
        // then
        ArgumentCaptor<Position> bonusCaptor = ArgumentCaptor.forClass(Position.class);
        verify(positionRepository).save(bonusCaptor.capture());
        Position updatedPosition = bonusCaptor.getValue();
        assertThat(updatedPosition.getName()).isEqualTo(existingPosition.getName());
        assertThat(updatedPosition.getSalary()).isEqualTo(positionDto.getSalary());
        assertThat(updatedPosition.getRoyalty()).isEqualTo(existingPosition.getRoyalty());
    }

    private void setModelMapper(PositionDto positionDto){
        when(modelMapper.map(positionDto,Position.class)).thenReturn(new Position(
                positionDto.getName(),
                positionDto.getSalary(),
                positionDto.getRoyalty()
        ));
    }
}