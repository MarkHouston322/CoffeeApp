package CoffeeApp.customerservice.services;

import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelDto;
import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelResponse;
import CoffeeApp.customerservice.exceptions.LoyaltyLevelAlreadyExistsException;
import CoffeeApp.customerservice.exceptions.ResourceNotFoundException;
import CoffeeApp.customerservice.models.LoyaltyLevel;
import CoffeeApp.customerservice.repositories.LoyaltyLevelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoyaltyLevelServiceTest {

    @Mock
    private LoyaltyLevelRepository loyaltyLevelRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LoyaltyLevelService loyaltyLevelService;

    @Test
    void shouldReturnLoyaltyLevelDtoById() {
        // given
        Integer loyaltyLevelId = 1;

        LoyaltyLevel level = new LoyaltyLevel();
        level.setId(loyaltyLevelId);
        level.setName("PRO");

        LoyaltyLevelDto levelDto = new LoyaltyLevelDto();
        levelDto.setName("PRO");

        when(loyaltyLevelRepository.findById(loyaltyLevelId)).thenReturn(Optional.of(level));
        when(modelMapper.map(level, LoyaltyLevelDto.class)).thenReturn(levelDto);
        // when
        LoyaltyLevelDto result = loyaltyLevelService.findById(loyaltyLevelId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(level.getName());
        verify(loyaltyLevelRepository).findById(loyaltyLevelId);
    }

    @Test
    void shouldNotEReturnLoyaltyLevelDtoByIdAndThrowException() {
        // given
        Integer levelId = 1;
        when(loyaltyLevelRepository.findById(levelId)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> loyaltyLevelService.findById(levelId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Loyalty level", "id", Integer.toString(levelId));
        verify(loyaltyLevelRepository).findById(levelId);
    }

    @Test
    void shouldReturnListOfLoyaltyLevelsByName() {
        // given
        String name = "Beg";
        LoyaltyLevel level1 = new LoyaltyLevel();
        level1.setName("Beginner");
        LoyaltyLevel level2 = new LoyaltyLevel();
        level2.setName("Beg");

        List<LoyaltyLevel> levels = Arrays.asList(level1,level2);

        LoyaltyLevelDto levelDto1 = new LoyaltyLevelDto();
        levelDto1.setName("Beginner");
        LoyaltyLevelDto levelDto2 = new LoyaltyLevelDto();
        levelDto2.setName("Beg");

        when(loyaltyLevelRepository.findByNameStartingWith(name)).thenReturn(levels);
        when(modelMapper.map(level1, LoyaltyLevelDto.class)).thenReturn(levelDto1);
        when(modelMapper.map(level2, LoyaltyLevelDto.class)).thenReturn(levelDto2);
        // when
        LoyaltyLevelResponse response = loyaltyLevelService.findByName(name);
        // then
        assertThat(response.getLoyaltyLevels()).containsExactly(levelDto1, levelDto2);
        verify(loyaltyLevelRepository).findByNameStartingWith(name);
    }

    @Test
    void shouldNotReturnListOfLoyaltyLevelsByName() {
        // given
        String name = "Beg";
        when(loyaltyLevelRepository.findByNameStartingWith(name)).thenReturn(new ArrayList<>());
        // when
        LoyaltyLevelResponse response = loyaltyLevelService.findByName(name);
        // then
        assertThat(response.getLoyaltyLevels()).isEmpty();
        verify(loyaltyLevelRepository).findByNameStartingWith(name);
    }

    @Test
    void shouldReturnAllLoyaltyLevels() {
        // given
        LoyaltyLevel level1 = new LoyaltyLevel();
        level1.setName("Beginner");
        LoyaltyLevel level2 = new LoyaltyLevel();
        level2.setName("Beg");

        List<LoyaltyLevel> levels = Arrays.asList(level1,level2);
        when(loyaltyLevelRepository.findAll()).thenReturn(levels);

        LoyaltyLevelDto levelDto1 = new LoyaltyLevelDto();
        levelDto1.setName("Beginner");
        LoyaltyLevelDto levelDto2 = new LoyaltyLevelDto();
        levelDto2.setName("Beg");

        when(modelMapper.map(level1, LoyaltyLevelDto.class)).thenReturn(levelDto1);
        when(modelMapper.map(level2, LoyaltyLevelDto.class)).thenReturn(levelDto2);
        // when
        LoyaltyLevelResponse response = loyaltyLevelService.findAll();
        // then
        assertThat(response.getLoyaltyLevels()).containsExactly(levelDto1, levelDto2);
        verify(loyaltyLevelRepository).findAll();
    }

    @Test
    void shouldNotReturnAllLoyaltyLevels() {
        // given
        when(loyaltyLevelRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        LoyaltyLevelResponse response = loyaltyLevelService.findAll();
        // then
        assertThat(response.getLoyaltyLevels()).isEmpty();
        verify(loyaltyLevelRepository).findAll();
    }

    @Test
    void shouldAddLoyaltyLevel() {
        // given
        LoyaltyLevelDto levelDto = new LoyaltyLevelDto("PRO", 1000, 0.1f);
        when(loyaltyLevelRepository.findByName(levelDto.getName())).thenReturn(Optional.empty());
        setModelMapper(levelDto);
        // when
        loyaltyLevelService.addLoyaltyLevel(levelDto);
        // then
        ArgumentCaptor<LoyaltyLevel> levelArgumentCaptor = ArgumentCaptor.forClass(LoyaltyLevel.class);
        verify(loyaltyLevelRepository).save(levelArgumentCaptor.capture());

        LoyaltyLevel capturedLevel = levelArgumentCaptor.getValue();
        assertThat(capturedLevel.getName()).isEqualTo(levelDto.getName());
        assertThat(capturedLevel.getEdge()).isEqualTo(levelDto.getEdge());
        assertThat(capturedLevel.getDiscountPercentage()).isEqualTo(levelDto.getDiscountPercentage());
    }

    @Test
    void shouldNotAddLoyaltyLevelAndThrowException(){
        // given
        LoyaltyLevelDto levelDto = new LoyaltyLevelDto("PRO", 1000, 0.1f);
        LoyaltyLevel existingLevel = new LoyaltyLevel();
        existingLevel.setName(levelDto.getName());

        when(loyaltyLevelRepository.findByName(levelDto.getName())).thenReturn(Optional.of(existingLevel));
        setModelMapper(levelDto);
        // when & then
        assertThatThrownBy(() -> loyaltyLevelService.addLoyaltyLevel(levelDto))
                .isInstanceOf(LoyaltyLevelAlreadyExistsException.class)
                .hasMessageContaining("Loyalty level has already been added with this name");
    }

    @Test
    void shouldDeleteLoyaltyLevelById(){
        // given
        Integer levelId = 1;
        LoyaltyLevel level = new LoyaltyLevel();
        level.setId(levelId);

        when(loyaltyLevelRepository.findById(levelId)).thenReturn(Optional.of(level));
        doNothing().when(loyaltyLevelRepository).deleteById(levelId);
        // when
        loyaltyLevelService.deleteLoyaltyLevel(levelId);
        // then
        verify(loyaltyLevelRepository, times(1)).findById(levelId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(loyaltyLevelRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(levelId);
    }

    @Test
    void shouldUpdateLoyaltyLevelById(){
        // given
        Integer levelId = 1;
        LoyaltyLevelDto levelDto = new LoyaltyLevelDto("New_PRO",1000,0.1f);
        LoyaltyLevel existingLevel = new LoyaltyLevel("PRO", 2000, 0.1f);
        existingLevel.setId(levelId);

        when(loyaltyLevelRepository.findById(levelId)).thenReturn(Optional.of(existingLevel));
        setModelMapper(levelDto);
        // when
        loyaltyLevelService.updateLoyaltyService(levelId,levelDto);
        // then;
        ArgumentCaptor<LoyaltyLevel> levelArgumentCaptor = ArgumentCaptor.forClass(LoyaltyLevel.class);
        verify(loyaltyLevelRepository).save(levelArgumentCaptor.capture());

        LoyaltyLevel savedLevel = levelArgumentCaptor.getValue();
        assertThat(savedLevel.getId()).isEqualTo(existingLevel.getId());
        assertThat(savedLevel.getName()).isEqualTo(levelDto.getName());
        assertThat(savedLevel.getEdge()).isEqualTo(levelDto.getEdge());
        assertThat(savedLevel.getDiscountPercentage()).isEqualTo(existingLevel.getDiscountPercentage());
    }



    private void setModelMapper(LoyaltyLevelDto levelDto) {
        when(modelMapper.map(levelDto, LoyaltyLevel.class)).thenReturn(new LoyaltyLevel(levelDto.getName(),
                levelDto.getEdge(),
                levelDto.getDiscountPercentage()));
    }

}