package CoffeeApp.storageservice.repositories.ingredientRepository;

import CoffeeApp.storageservice.models.ingredient.Ingredient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class IngredientRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("ingredient.sql");

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp(){
        List<Ingredient> ingredients = new ArrayList<>(Arrays.asList(
                new Ingredient("Milk",90f,3000f),
                new Ingredient("Milk Coconut",90f,3000f)
        ));
        ingredientRepository.saveAll(ingredients);
    }

    @AfterEach
    void clearUp(){
        ingredientRepository.deleteAll();
    }

    @Test
    void shouldFindIngredientsByNameStartingWith(){
        List<Ingredient> ingredients = ingredientRepository.findByNameStartingWith("Mi");
        assertThat(ingredients).isNotEmpty();
    }

    @Test
    void shouldNotFindIngredientsByNameStartingWith(){
        List<Ingredient> ingredients = ingredientRepository.findByNameStartingWith("Wa");
        assertThat(ingredients).isEmpty();
    }

    @Test
    void shouldFindIngredientByName(){
        Optional<Ingredient> ingredient = ingredientRepository.findByName("Milk");
        assertThat(ingredient).isPresent();
    }

    @Test
    void shouldNotFindIngredientByName(){
        Optional<Ingredient> ingredient = ingredientRepository.findByName("Water");
        assertThat(ingredient).isEmpty();
    }

}