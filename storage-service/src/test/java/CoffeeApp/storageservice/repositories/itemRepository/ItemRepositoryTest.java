package CoffeeApp.storageservice.repositories.itemRepository;

import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.repositories.CategoryRepository;
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
class ItemRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("item.sql");

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @BeforeEach
    void setUp(){
        Category category = new Category("Drinks");
        categoryRepository.save(category);
        List<Item> items = Arrays.asList(
                new Item("Water",2f,50f,category),
                new Item("Whiskey",2f,50f,category)
        );
        itemRepository.saveAll(items);
    }

    @AfterEach
    void clearUp(){
        itemRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldReturnItemsByNameStartingWith(){
        List<Item> items = itemRepository.findByNameStartingWith("W");
        assertThat(items).isNotEmpty();
    }

    @Test
    void shouldNotReturnItemsByNameStartingWith(){
        List<Item> items = itemRepository.findByNameStartingWith("Mi");
        assertThat(items).isEmpty();
    }

    @Test
    void shouldReturnItemByName(){
        Optional<Item> item = itemRepository.findByName("Water");
        assertThat(item).isPresent();
    }

    @Test
    void shouldNotReturnItemByName(){
        Optional<Item> item = itemRepository.findByName("Milk");
        assertThat(item).isEmpty();
    }

}