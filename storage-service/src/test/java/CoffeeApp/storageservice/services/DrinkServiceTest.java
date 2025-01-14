package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.drinkDto.AddDrinkDto;
import CoffeeApp.storageservice.dto.drinkDto.DrinkResponse;
import CoffeeApp.storageservice.dto.drinkDto.ShowDrinkDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.messages.GoodMessage;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.DrinkAlreadyExistsException;
import CoffeeApp.storageservice.interfaces.ContainIngredients;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.Drink;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.repositories.DrinkRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientInDrinkService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrinkServiceTest {

    @Mock
    private DrinkRepository drinkRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private IngredientInDrinkService ingredientInDrinkService;

    @Mock
    private ItemService itemService;

    @Mock
    private ContainIngredients containIngredients;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private DrinkService drinkService;

    @Test
    void shouldReturnDrinkDtoById(){
        // given
        Category category = new Category();
        Drink drink = new Drink("Latte",100,50f,category,1,1,2f);
        ShowDrinkDto showDrinkDto = new ShowDrinkDto("Latte",100,50f,category,1,1,2f);
        when(drinkRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(drink));
        when(modelMapper.map(drink, ShowDrinkDto.class)).thenReturn(showDrinkDto);
        // when
        ShowDrinkDto result = drinkService.findById(Mockito.anyInt());
        // then
        assertThat(result.getName()).isEqualTo(drink.getName());
        verify(drinkRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnDrinkDtoById() {
        // given
        int id = 1;
        when(drinkRepository.findById(id)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> drinkService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Drink", "id", Integer.toString(id));
        verify(drinkRepository).findById(id);
    }

    @Test
    void shouldReturnAllDrinksDto() {
        // given
        Category category = new Category();
        Drink drink1 = new Drink("Latte",100,50f,category,1,1,2f);
        Drink drink2 = new Drink("Espresso",100,50f,category,1,1,2f);
        List<Drink> drinks = Arrays.asList(drink1,drink2);
        ShowDrinkDto showDrinkDto1 = new ShowDrinkDto("Latte",100,50f,category,1,1,2f);
        ShowDrinkDto showDrinkDto2 = new ShowDrinkDto("Espresso",100,50f,category,1,1,2f);
        when(drinkRepository.findAll()).thenReturn(drinks);
        when(modelMapper.map(drink1, ShowDrinkDto.class)).thenReturn(showDrinkDto1);
        when(modelMapper.map(drink2, ShowDrinkDto.class)).thenReturn(showDrinkDto2);
        // when
        DrinkResponse response = drinkService.findAll();
        // then
        assertThat(response.getDrinks()).containsExactly(showDrinkDto1,showDrinkDto2);
        verify(drinkRepository).findAll();
    }

    @Test
    void shouldNotReturnAllDrinksDto(){
        // given
        when(drinkRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        DrinkResponse response = drinkService.findAll();
        // then
        assertThat(response.getDrinks()).isEmpty();
        verify(drinkRepository).findAll();
    }

    @Test
    void shouldAddDrink() throws ExecutionException, InterruptedException {
        // given
        Category category = new Category();
        AddDrinkDto drinkDto = new AddDrinkDto("Latte",category, 1.5f);
        Map<String, String> ingredients = Map.of("Milk", "0.5", "Coffee", "0.2");

        Drink drinkToAdd = new Drink();
        drinkToAdd.setName(drinkDto.getName());
        drinkToAdd.setSurchargeRatio(drinkDto.getSurchargeRatio());

        when(drinkRepository.findByName(drinkDto.getName())).thenReturn(Optional.empty());
        setModelMapper(drinkDto);
        when(containIngredients.checkGoods(ingredients, ingredientService, itemService)).thenReturn(new GoodsWrapperForWriteOff(new ConcurrentHashMap<>(),new ConcurrentHashMap<>()));
        when(ingredientService.calculateCost(ingredients)).thenReturn(100.0f);
        doNothing().when(ingredientInDrinkService).addIngredientInDrink(Mockito.any());
        // when
        drinkService.addDrink(drinkDto, ingredients);
        // then
        verify(drinkRepository).save(drinkToAdd);
        assertEquals(100.0f, drinkToAdd.getCostPrice());
        assertEquals(1000, drinkToAdd.getPrice());

        verify(ingredientService).calculateCost(ingredients);
        verify(ingredientService).findByName("Milk");
        verify(ingredientService).findByName("Coffee");
        verify(drinkRepository).findByName(drinkDto.getName());
        verify(ingredientInDrinkService, times(2)).addIngredientInDrink(Mockito.any());
        testSendDrink(drinkToAdd);
    }

    @Test
    void shouldNotAddDrink(){
        // given
        Category category = new Category();
        AddDrinkDto drinkDto = new AddDrinkDto("Latte",category, 1.5f);
        Drink existingDrink = new Drink();
        when(drinkRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(existingDrink));
        setModelMapper(drinkDto);
        // when & then
        assertThatThrownBy(() -> drinkService.addDrink(Mockito.any(AddDrinkDto.class),Mockito.anyMap()))
                .isInstanceOf(DrinkAlreadyExistsException.class)
                .hasMessageContaining("Drink has already been added with this name");
    }

    @Test
    void shouldDeleteDrinkById(){
        // given
        int id = 1;
        Drink drink = new Drink();
        when(drinkRepository.findById(id)).thenReturn(Optional.of(drink));
        doNothing().when(drinkRepository).deleteById(id);
        // when
        drinkService.deleteDrinkById(id);
        // then
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(drinkRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
        verify(drinkRepository).findById(id);
    }

    @Test
    void shouldUpdateDrink() throws Exception {
        // given
        int drinkId = 1;
        Category category = new Category();
        AddDrinkDto addDrinkDto = new AddDrinkDto("Latte",category, 1.5f);
        Map<String, String> ingredients = Map.of("Milk", "0.5", "Coffee", "0.2");

        Drink drinkToBeUpdated = new Drink();
        drinkToBeUpdated.setId(drinkId);
        drinkToBeUpdated.setSoldQuantity(10);
        drinkToBeUpdated.setWriteOffQuantity(2);

        Drink updatedDrink = new Drink();
        updatedDrink.setId(drinkId);
        updatedDrink.setName(addDrinkDto.getName());
        updatedDrink.setSurchargeRatio(addDrinkDto.getSurchargeRatio());
        updatedDrink.setSoldQuantity(drinkToBeUpdated.getSoldQuantity());
        updatedDrink.setWriteOffQuantity(drinkToBeUpdated.getWriteOffQuantity());

        Ingredient ingredientMilk = new Ingredient("Milk", 50.0f,10f);
        Ingredient ingredientCoffee = new Ingredient("Coffee", 100.0f,10f);

        when(drinkRepository.findById(drinkId)).thenReturn(Optional.of(drinkToBeUpdated));
        when(modelMapper.map(addDrinkDto,Drink.class)).thenReturn(updatedDrink);
        when(ingredientService.calculateCost(ingredients)).thenReturn(200.0f);
        when(ingredientService.findByName("Milk")).thenReturn(ingredientMilk);
        when(ingredientService.findByName("Coffee")).thenReturn(ingredientCoffee);
        doNothing().when(ingredientInDrinkService).deleteIngredientInDrink(drinkId);
        doNothing().when(ingredientInDrinkService).addIngredientInDrink(Mockito.any());
        when(drinkRepository.save(updatedDrink)).thenReturn(updatedDrink);
        // when
        boolean result = drinkService.updateDrink(drinkId, addDrinkDto, ingredients);
        // then
        assertTrue(result);
        verify(drinkRepository,times(2)).findById(drinkId);
        verify(ingredientService).calculateCost(ingredients);
        verify(ingredientService).findByName("Milk");
        verify(ingredientService).findByName("Coffee");
        verify(ingredientInDrinkService).deleteIngredientInDrink(drinkId);
        verify(ingredientInDrinkService, times(2)).addIngredientInDrink(Mockito.any());
        verify(drinkRepository).save(updatedDrink);
        testSendDrink(updatedDrink);
    }

    @Test
    void shouldReturnIngredientsByDrinkId() {
        // given
        Integer drinkId = 1;
        List<IngredientProjection> projections = List.of(
                new IngredientProjection() {
                    @Override
                    public String ingredientName() {
                        return "Milk";
                    }

                    @Override
                    public float ingredientQuantity() {
                        return 0.5f;
                    }
                },
                new IngredientProjection() {
                    @Override
                    public String ingredientName() {
                        return "Coffee";
                    }

                    @Override
                    public float ingredientQuantity() {
                        return 0.2f;
                    }
                }
        );

        when(drinkRepository.findIngredientsByDrinkId(drinkId)).thenReturn(projections);
        // expected result
        List<IngredientInDto> expectedIngredients = List.of(
                new IngredientInDto("Milk", 0.5f),
                new IngredientInDto("Coffee", 0.2f)
        );

        // when
        List<IngredientInDto> result = drinkService.getIngredientsByDrinkId(drinkId);
        // then
        assertEquals(expectedIngredients, result);
        verify(drinkRepository).findIngredientsByDrinkId(drinkId);
    }

    @Test
    void shouldReturnEmptyListWhenNoIngredientsFound() {
        // given
        Integer drinkId = 1;
        when(drinkRepository.findIngredientsByDrinkId(drinkId)).thenReturn(Collections.emptyList());
        // when
        List<IngredientInDto> result = drinkService.getIngredientsByDrinkId(drinkId);
        // then
        assertTrue(result.isEmpty());
        verify(drinkRepository).findIngredientsByDrinkId(drinkId);
    }

    @Test
    void shouldSellDrinkAndDecreaseIngredients() {
        // given
        String drinkName = "Latte";
        int quantityToSell = 2;

        Drink drink = new Drink();
        drink.setId(1);
        drink.setName(drinkName);
        drink.setSoldQuantity(10);

        List<IngredientInDrink> ingredients = List.of(
                new IngredientInDrink(new Ingredient( "Milk", 100f,1000f), drink, 0.5f),
                new IngredientInDrink(new Ingredient( "Coffee", 50f,1000f), drink, 0.2f)
        );
        when(drinkRepository.findByName(drinkName)).thenReturn(Optional.of(drink));
        when(ingredientInDrinkService.findByDrink_Id(drink.getId())).thenReturn(ingredients);
        // when
        drinkService.sellDrink(drinkName, quantityToSell);
        // then
        assertEquals(12, drink.getSoldQuantity());
        verify(drinkRepository).findByName(drinkName);
        verify(ingredientInDrinkService).findByDrink_Id(drink.getId());
        verify(ingredientService).decreaseIngredient(1, 0.5f * quantityToSell);
        verify(ingredientService).decreaseIngredient(2, 0.2f * quantityToSell);
    }

    @Test
    void shouldThrowExceptionWhenNoIngredientsFoundForDrink() {
        // given
        String drinkName = "Latte";
        Drink drink = new Drink();
        drink.setId(1);
        drink.setName(drinkName);
        when(drinkRepository.findByName(drinkName)).thenReturn(Optional.of(drink));
        when(ingredientInDrinkService.findByDrink_Id(drink.getId())).thenReturn(Collections.emptyList());
        // when & then
        assertThrows(ResourceNotFoundException.class, () -> drinkService.sellDrink(drinkName, 2));
        // Verify interaction
        verify(drinkRepository).findByName(drinkName);
        verify(ingredientInDrinkService).findByDrink_Id(drink.getId());
        verifyNoInteractions(ingredientService);
    }





    private void setModelMapper(AddDrinkDto addDrinkDto){
        when(modelMapper.map(addDrinkDto,Drink.class)).thenReturn(new Drink(addDrinkDto.getName(),addDrinkDto.getCategory(),addDrinkDto.getSurchargeRatio()));
    }

    private void testSendDrink(Drink drink){
        ArgumentCaptor<GoodMessage> drinkCapture = ArgumentCaptor.forClass(GoodMessage.class);
        verify(streamBridge).send(eq("sendGood-out-0"),drinkCapture.capture());
        GoodMessage drinkMessage = drinkCapture.getValue();
        assertThat(drinkMessage.getName()).isEqualTo(drink.getName());
    }

}