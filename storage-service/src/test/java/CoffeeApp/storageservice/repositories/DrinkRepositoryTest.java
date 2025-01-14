package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.Drink;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class DrinkRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("drink.sql");

    @Autowired
    private DrinkRepository drinkRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(){
        Category category = new Category("Drinks");
        categoryRepository.save(category);
        List<Drink> drinks = Arrays.asList(
                new Drink("Latte",100,50f,category,1,1,2f),
                new Drink("Latte Macchiato",100,50f,category,1,1,2f)
        );
        drinkRepository.saveAll(drinks);
    }

    @AfterEach
    void clearUp(){
        categoryRepository.deleteAll();
        drinkRepository.deleteAll();
    }

    @Test
    void shouldReturnDrinksByNameStartingWith(){
        List<Drink> drinks = drinkRepository.findByNameStartingWith("Lat");
        assertThat(drinks).isNotEmpty();
    }

    @Test
    void shouldNotReturnDrinksByNameStartingWith(){
        List<Drink> drinks = drinkRepository.findByNameStartingWith("Espr");
        assertThat(drinks).isEmpty();
    }

    @Test
    void shouldReturnDrinkByName(){
        Optional<Drink> drink = drinkRepository.findByName("Latte");
        assertThat(drink).isPresent();
    }

    @Test
    void shouldNotReturnDrinkByName(){
        Optional<Drink> drink = drinkRepository.findByName("Latte");
        assertThat(drink).isEmpty();
    }



}