package CoffeeApp.storageservice.services.ingredientService;

import CoffeeApp.storageservice.models.Drink;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientInDrinkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientInDrinkServiceTest {

    @Mock
    private IngredientInDrinkRepository ingredientInDrinkRepository;

    @InjectMocks
    private IngredientInDrinkService ingredientInDrinkService;

    @Test
    void shouldReturnIngredientsInOrderById(){
        // given
        IngredientInDrink ingredient1 = new IngredientInDrink();
        IngredientInDrink ingredient2 = new IngredientInDrink();
        List<IngredientInDrink> ingredients = Arrays.asList(ingredient1,ingredient2);
        when(ingredientInDrinkRepository.findByDrink_Id(Mockito.anyInt())).thenReturn(ingredients);
        // when
        List<IngredientInDrink> result = ingredientInDrinkService.findByDrink_Id(Mockito.anyInt());
        // then
        assertThat(result).containsExactly(ingredient1,ingredient2);
        verify(ingredientInDrinkRepository).findByDrink_Id(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnIngredientsInOrderById(){
        // given
        when(ingredientInDrinkRepository.findByDrink_Id(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // when
        List<IngredientInDrink> result = ingredientInDrinkService.findByDrink_Id(Mockito.anyInt());
        // then
        assertThat(result).isEmpty();
        verify(ingredientInDrinkRepository).findByDrink_Id(Mockito.anyInt());
    }

    @Test
    void shouldAddIngredientInDrink(){
        // given
        IngredientInDrink ingredientInDrink = new IngredientInDrink(new Ingredient(),new Drink(),2f);
        // when
        ingredientInDrinkService.addIngredientInDrink(ingredientInDrink);
        // then
        ArgumentCaptor<IngredientInDrink> ingredientCaptor = ArgumentCaptor.forClass(IngredientInDrink.class);
        verify(ingredientInDrinkRepository).save(ingredientCaptor.capture());
        IngredientInDrink result = ingredientCaptor.getValue();
        assertThat(result.getDrink()).isEqualTo(ingredientInDrink.getDrink());
        assertThat(result.getQuantity()).isEqualTo(ingredientInDrink.getQuantity());
    }

}