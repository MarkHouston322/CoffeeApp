package CoffeeApp.storageservice.repositories.itemRepository;

import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInFridge;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ItemInFridgeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("item_in_fridge.sql");

    @Autowired
    private ItemInFridgeRepository itemInFridgeRepository;

    private final Category category = new Category();
    private final Item item = new Item("Water",2f,50f,category);

    @AfterEach
    void clearUp(){
        itemInFridgeRepository.deleteAll();
    }

    @Test
    void shouldReturnItemInFridge(){
        ItemInFridge itemInFridge = new ItemInFridge(item, 1000L);
        itemInFridgeRepository.save(itemInFridge);
        Optional<ItemInFridge> result = itemInFridgeRepository.isItemInFridge(item);
        assertThat(result).isPresent();
    }

    @Test
    void shouldNotReturnItemInFridge(){
        Optional<ItemInFridge> itemInFridge = itemInFridgeRepository.isItemInFridge(new Item());
        assertThat(itemInFridge).isEmpty();
    }

    @Test
    void shouldReturnAllItemsInFridge(){
        ItemInFridge itemInFridge = new ItemInFridge(item, 1000L);
        itemInFridgeRepository.save(itemInFridge);
        List<ItemInFridge> items = itemInFridgeRepository.getItemsInFridge();
        assertThat(items).isNotEmpty();
    }

    @Test
    void shouldNotReturnAllItemsInFridge(){
        List<ItemInFridge> items = itemInFridgeRepository.getItemsInFridge();
        assertThat(items).isEmpty();
    }

    @Test
    void shouldReturnItemInFridgeByItemId(){
        item.setId(1);
        Optional<ItemInFridge> itemInFridge = itemInFridgeRepository.findByItemId(1);
        assertThat(itemInFridge).isPresent();
    }
}