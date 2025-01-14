package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.models.Category;
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

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CategoryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("category.sql");

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp(){
        List<Category> categories = Arrays.asList(
                new Category("Drinks"),
                new Category("Drinks still")
        );
        categoryRepository.saveAll(categories);
    }

    @AfterEach
    void clearUp(){
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnCategoriesByNameStartingWith(){
        List<Category> categories = categoryRepository.findByNameStartingWith("Dr");
        assertThat(categories).isNotEmpty();
    }

    @Test
    void shouldNotReturnCategoriesByNameStartingWith(){
        List<Category> categories = categoryRepository.findByNameStartingWith("Co");
        assertThat(categories).isEmpty();
    }

    @Test
    void shouldReturnCategoryByName(){
        Optional<Category> category = categoryRepository.findByName("Drink");
        assertThat(category).isPresent();
    }

    @Test
    void shouldNotReturnCategoryByName(){
        Optional<Category> category = categoryRepository.findByName("Cookies");
        assertThat(category).isEmpty();
    }

}