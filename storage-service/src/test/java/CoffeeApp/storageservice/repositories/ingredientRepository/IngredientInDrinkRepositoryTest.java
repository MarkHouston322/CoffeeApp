package CoffeeApp.storageservice.repositories.ingredientRepository;

import CoffeeApp.storageservice.models.Drink;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers

class IngredientInDrinkRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("ingredient_in_drink.sql");

    @Autowired
    private IngredientInDrinkRepository ingredientInDrinkRepository;

    private static final Drink drink = new Drink();
    private static final Ingredient ingredient = new Ingredient();

    @BeforeAll
    public static void setId(){
        drink.setId(1);
        ingredient.setId(1);
    }

    @BeforeEach
    void setUp(){
        IngredientInDrink ingredientInDrink = new IngredientInDrink(ingredient,drink,2f);
        ingredientInDrinkRepository.save(ingredientInDrink);
    }

    @BeforeEach
    void clearUp(){
        ingredientInDrinkRepository.deleteAll();
    }

    @Test
    void shouldFindIngredientByDrinkId(){
        List<IngredientInDrink> ingredientInDrinks = ingredientInDrinkRepository.findByDrink_Id(drink.getId());
        assertThat(ingredientInDrinks).isNotEmpty();
    }

    @Test
    void shouldNotFindIngredientByDrinkId(){
        List<IngredientInDrink> ingredientInDrinks = ingredientInDrinkRepository.findByDrink_Id(2);
        assertThat(ingredientInDrinks).isEmpty();
    }

    @Test
    void shouldDeleteIngredientByDrinkId() {
        ingredientInDrinkRepository.deleteByDrink_Id(drink.getId());
        List<IngredientInDrink> ingredientInDrinks = ingredientInDrinkRepository.findByDrink_Id(drink.getId());
        assertThat(ingredientInDrinks).isEmpty();
    }

    @Test
    void shouldNotDeleteIngredientByDrinkId() {
        ingredientInDrinkRepository.deleteByDrink_Id(2);
        List<IngredientInDrink> ingredientInDrinks = ingredientInDrinkRepository.findByDrink_Id(drink.getId());
        assertThat(ingredientInDrinks).isNotEmpty();
    }
}