package CoffeeApp.storageservice.services.ingredientService;

import CoffeeApp.storageservice.dto.ingredientDto.IngredientDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientResponse;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.IngredientAlreadyExistsException;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private IngredientService ingredientService;


    @Test
    void shouldReturnIngredientDtoById(){
        // given
        Ingredient ingredient = new Ingredient("Milk",90f,3000f);
        IngredientDto ingredientDto = new IngredientDto("Milk",90f,3000f);
        when(modelMapper.map(ingredient,IngredientDto.class)).thenReturn(ingredientDto);
        when(ingredientRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(ingredient));
        // when
        IngredientDto result = ingredientService.findById(Mockito.anyInt());
        // then
        assertThat(result.getName()).isEqualTo(ingredient.getName());
        verify(ingredientRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnIngredientDtoById(){
        // given
        int id = 1;
        when(ingredientRepository.findById(id)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> ingredientService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ingredient", "id", Integer.toString(id));
        verify(ingredientRepository).findById(id);
    }

    @Test
    void shouldReturnIngredientDtoByName(){
        // given
        Ingredient ingredient = new Ingredient("Milk",90f,3000f);
        when(ingredientRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(ingredient));
        // when
        Ingredient result = ingredientService.findByName(Mockito.anyString());
        // then
        assertThat(result.getName()).isEqualTo(ingredient.getName());
        verify(ingredientRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldNotReturnIngredientDtoByName(){
        // given
        String name = "Milk";
        when(ingredientRepository.findByName(name)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> ingredientService.findByName(name))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ingredient", "name", name);
        verify(ingredientRepository).findByName(name);
    }

    @Test
    void shouldReturnIngredientsDtoByNameStartingWith(){
        // given
        Ingredient ingredient1 = new Ingredient("Milk",90f,3000f);
        Ingredient ingredient2 = new Ingredient("Milk Coconut",90f,3000f);
        List<Ingredient> ingredients = Arrays.asList(ingredient1,ingredient2);
        IngredientDto ingredientDto1 = new IngredientDto("Milk",90f,3000f);
        IngredientDto ingredientDto2 = new IngredientDto("Milk Coconut",90f,3000f);
        when(modelMapper.map(ingredient1,IngredientDto.class)).thenReturn(ingredientDto1);
        when(modelMapper.map(ingredient2,IngredientDto.class)).thenReturn(ingredientDto2);
        when(ingredientRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(ingredients);
        // when
        IngredientResponse response = ingredientService.findIngredientsByName(Mockito.anyString());
        // then
        assertThat(response.getIngredients()).containsExactly(ingredientDto1,ingredientDto2);
        verify(ingredientRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldNotReturnIngredientsDtoByNameStartingWith(){
        // given
        when(ingredientRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(Collections.emptyList());
        // when
        IngredientResponse response = ingredientService.findIngredientsByName(Mockito.anyString());
        // then
        assertThat(response.getIngredients()).isEmpty();
        verify(ingredientRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldReturnAllIngredientsDto(){
        // given
        Ingredient ingredient1 = new Ingredient("Milk",90f,3000f);
        Ingredient ingredient2 = new Ingredient("Milk Coconut",90f,3000f);
        List<Ingredient> ingredients = Arrays.asList(ingredient1,ingredient2);
        IngredientDto ingredientDto1 = new IngredientDto("Milk",90f,3000f);
        IngredientDto ingredientDto2 = new IngredientDto("Milk Coconut",90f,3000f);
        when(modelMapper.map(ingredient1,IngredientDto.class)).thenReturn(ingredientDto1);
        when(modelMapper.map(ingredient2,IngredientDto.class)).thenReturn(ingredientDto2);
        when(ingredientRepository.findAll()).thenReturn(ingredients);
        // when
        IngredientResponse response = ingredientService.findAll();
        // then
        assertThat(response.getIngredients()).containsExactly(ingredientDto1,ingredientDto2);
        verify(ingredientRepository).findAll();
    }

    @Test
    void shouldNotReturnAllIngredientsDto(){
        // given
        when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        IngredientResponse response = ingredientService.findAll();
        // then
        assertThat(response.getIngredients()).isEmpty();
        verify(ingredientRepository).findAll();
    }

    @Test
    void shouldAddIngredient(){
        // given
        IngredientDto ingredientDto = new IngredientDto("Milk",90f,3000f);
        when(ingredientRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        setModelMapper(ingredientDto);
        // when
        ingredientService.addIngredient(ingredientDto);
        // then
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient result = ingredientCaptor.getValue();
        assertThat(result.getName()).isEqualTo(ingredientDto.getName());
    }

    @Test
    void shouldNotAddIngredient(){
        // given
        IngredientDto ingredientDto = new IngredientDto("Milk",90f,3000f);
        Ingredient existingIngredient = new Ingredient();
        when(ingredientRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(existingIngredient));
        setModelMapper(ingredientDto);
        // when & then
        assertThatThrownBy(() -> ingredientService.addIngredient(ingredientDto))
                .isInstanceOf(IngredientAlreadyExistsException.class)
                .hasMessageContaining("Ingredient has already been added with this name");
        verify(ingredientRepository).findByName(Mockito.anyString());

    }

    @Test
    void shouldDeleteIngredientById(){
        // given
        int id = 1;
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        when(ingredientRepository.findById(id)).thenReturn(Optional.of(ingredient));
        doNothing().when(ingredientRepository).deleteById(id);
        // when
        ingredientService.deleteIngredient(id);
        // then
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(ingredientRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
        verify(ingredientRepository).findById(id);
    }

    @Test
    void shouldUpdateIngredientById(){
        // given
        IngredientDto ingredientDto = new IngredientDto("Milk",90f,3000f);
        Ingredient existingIngredient = new Ingredient("Milk 3.2",100f,3000f);
        when(ingredientRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(existingIngredient));
        setModelMapper(ingredientDto);
        // when
        ingredientService.updateIngredient(Mockito.anyInt(),ingredientDto);
        // then
        ArgumentCaptor<Ingredient> ingredientCaptor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientRepository).save(ingredientCaptor.capture());
        Ingredient updatedIngredient = ingredientCaptor.getValue();
        assertThat(updatedIngredient.getName()).isEqualTo(existingIngredient.getName());
        assertThat(updatedIngredient.getCostPerOneKilo()).isEqualTo(ingredientDto.getCostPerOneKilo());
        verify(ingredientRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldDecreaseIngredientQuantityById(){
        // given
        float quantity = 1000f;
        Ingredient ingredient = new Ingredient("Milk",90f,3000f);
        when(ingredientRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(ingredient));
        // when
        ingredientService.decreaseIngredient(Mockito.anyInt(),quantity);
        // then
        assertThat(ingredient.getQuantityInStock()).isEqualTo(2000f);
        verify(ingredientRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldDecreaseIngredientQuantityByName(){
        // given
        float quantity = 1000f;
        Ingredient ingredient = new Ingredient("Milk",90f,3000f);
        when(ingredientRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(ingredient));
        // when
        ingredientService.decreaseIngredient(Mockito.anyString(),quantity);
        // then
        assertThat(ingredient.getQuantityInStock()).isEqualTo(2000f);
        verify(ingredientRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldIncreaseIngredientQuantityByName(){
        // given
        float quantity = 1000f;
        Ingredient ingredient = new Ingredient("Milk",90f,3000f);
        when(ingredientRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(ingredient));
        // when
        ingredientService.increaseIngredient(Mockito.anyString(),quantity);
        // then
        assertThat(ingredient.getQuantityInStock()).isEqualTo(4000f);
        verify(ingredientRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldCalculateCostSuccessfully() {
        // given
        Map<String, String> ingredients = Map.of(
                "Milk", "2.0",
                "Coffee", "1.5"
        );
        Ingredient milk = new Ingredient("Milk",50f,3000f);
        Ingredient coffee = new Ingredient("Coffee",30f,3000f);
        when(ingredientRepository.findByName("Milk")).thenReturn(Optional.of(milk));
        when(ingredientRepository.findByName("Coffee")).thenReturn(Optional.of(coffee));
        // when
        float totalCost = ingredientService.calculateCost(ingredients);
        // then
        assertEquals(145.0f, totalCost, 0.01f);
        verify(ingredientRepository).findByName("Milk");
        verify(ingredientRepository).findByName("Coffee");
    }




    private void setModelMapper(IngredientDto ingredientDto){
        when(modelMapper.map(ingredientDto,Ingredient.class)).thenReturn(new Ingredient(ingredientDto.getName(),
                ingredientDto.getCostPerOneKilo(),ingredientDto.getQuantityInStock()));
    }


}