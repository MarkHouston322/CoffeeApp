package CoffeeApp.storageservice.interfaces;

import CoffeeApp.storageservice.exceptions.NullOrEmptyParamException;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.services.AcceptanceService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContainIngredientsTest {

    @InjectMocks
    private AcceptanceService acceptanceService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private ItemService itemService;

    @Test
    void shouldDistributeGoodsBetweenIngredientsAndItems() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, String> goods = Map.of(
                "Sugar", "5",
                "Cup", "10"
        );

        when(ingredientService.findByName("Sugar")).thenReturn(new Ingredient("Sugar",100f,1000f));
        when(itemService.findByName("Cup")).thenReturn(new Item("Cup",100,1000f));

        // Act
        GoodsWrapperForWriteOff result = acceptanceService.checkGoods(goods, ingredientService, itemService);

        // Assert
        assertThat(result.ingredientResults())
                .containsEntry("Sugar", "5")
                .doesNotContainKey("Cup");

        assertThat(result.itemResults())
                .containsEntry("Cup", "10")
                .doesNotContainKey("Sugar");
    }

    @Test
    void shouldThrowExceptionForEmptyQuantity() {
        // Arrange
        Map<String, String> goods = Map.of("Sugar", "");

        when(ingredientService.findByName("Sugar")).thenReturn(new Ingredient("Sugar",100f,1000f));

        // Act & Assert
        assertThatThrownBy(() -> acceptanceService.checkGoods(goods, ingredientService, itemService))
                .isInstanceOf(CompletionException.class)
                .hasMessageContaining("Ingredient should not be empty or has null quantity");
    }

    @Test
    void shouldFallbackToItemsWhenIngredientNotFound() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, String> goods = Map.of("Cup", "10");

        when(ingredientService.findByName("Cup")).thenThrow(new ResourceNotFoundException("Item", "name", "Cup"));
        when(itemService.findByName("Cup")).thenReturn(new Item("Cup",100,1000f));

        // Act
        GoodsWrapperForWriteOff result = acceptanceService.checkGoods(goods, ingredientService, itemService);

        // Assert
        assertThat(result.itemResults())
                .containsEntry("Cup", "10");
        assertThat(result.ingredientResults())
                .doesNotContainKey("Cup");
    }

    @Test
    void shouldProcessGoodsAsynchronously() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, String> goods = Map.of(
                "Sugar", "5",
                "Cup", "10"
        );

        when(ingredientService.findByName(anyString()))
                .thenAnswer(invocation -> {
                    Thread.sleep(100); // Simulate delay
                    if (invocation.getArgument(0).equals("Sugar")) {
                        return new Ingredient("Sugar",100f,1000f);
                    }
                    throw new ResourceNotFoundException("Ingredient", "name", "Sugar");
                });

        when(itemService.findByName("Cup")).thenReturn(new Item("Cup",100,1000f));

        // Act
        GoodsWrapperForWriteOff result = acceptanceService.checkGoods(goods, ingredientService, itemService);

        // Assert
        assertThat(result.ingredientResults())
                .containsEntry("Sugar", "5");

        assertThat(result.itemResults())
                .containsEntry("Cup", "10");
    }

}