package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.WriteOff;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.ingredient.IngredientInWriteOff;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInWriteOff;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientInWriteOffRepository;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientRepository;
import CoffeeApp.storageservice.repositories.itemRepository.ItemInWriteOffRepository;
import CoffeeApp.storageservice.repositories.itemRepository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
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
class WriteOffRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("schema.sql");

    @Autowired
    private WriteOffRepository writeOffRepository;

    @Autowired
    private IngredientInWriteOffRepository ingredientInWriteOffRepository;

    @Autowired
    private ItemInWriteOffRepository itemInWriteOffRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        WriteOff writeOff = new WriteOff();
        writeOffRepository.save(writeOff);

        Category category = new Category();
        categoryRepository.save(category);

        Ingredient ingredient = new Ingredient("Milk",90f,1000f);
        ingredientRepository.save(ingredient);

        Item item = new Item("Water",2f,50f,category);
        itemRepository.save(item);

        IngredientInWriteOff ingredientInWriteOff = new IngredientInWriteOff(writeOff, ingredient, 5f);
        ingredientInWriteOffRepository.save(ingredientInWriteOff);

        ItemInWriteOff itemInWriteOff = new ItemInWriteOff(writeOff, item, 2f);
        itemInWriteOffRepository.save(itemInWriteOff);
    }

    @AfterEach
    void tearDown() {
        ingredientInWriteOffRepository.deleteAll();
        itemInWriteOffRepository.deleteAll();
        ingredientRepository.deleteAll();
        itemRepository.deleteAll();
        writeOffRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindIngredientsByWriteOffId() {
        // when
        List<IngredientProjection> ingredients = writeOffRepository.findIngredientsByWriteOffId(1);

        // then
        assertThat(ingredients).hasSize(1);
        IngredientProjection ingredient = ingredients.get(0);
        assertThat(ingredient.ingredientName()).isEqualTo("Sugar");
        assertThat(ingredient.ingredientQuantity()).isEqualTo(5);
    }

    @Test
    void shouldFindItemsByWriteOffId() {
        // when
        List<ItemProjection> items = writeOffRepository.findItemsByWriteOffId(1);

        // then
        assertThat(items).hasSize(1);
        ItemProjection item = items.get(0);
        assertThat(item.itemName()).isEqualTo("Cup");
        assertThat(item.itemQuantity()).isEqualTo(2);
        assertThat(item.itemCostPrice()).isEqualTo(10);
    }
}
