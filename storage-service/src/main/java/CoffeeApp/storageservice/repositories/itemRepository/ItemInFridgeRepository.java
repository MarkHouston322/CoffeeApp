package CoffeeApp.storageservice.repositories.itemRepository;

import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInFridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemInFridgeRepository extends JpaRepository<ItemInFridge, Integer> {

    @Query("select p from ItemInFridge p where p.item.name = :itemName")
    List<ItemInFridge> findByItemName(@Param("itemName") String name);

    @Query("select p from ItemInFridge p where p.item = :item and p.isSold = false and p.expired = false")
    Optional<ItemInFridge> isItemInFridge(@Param("item") Item item);

    @Query("select p from ItemInFridge p where p.isSold = false and p.expired = false")
    List<ItemInFridge> getItemsInFridge();

    @Query("select p from ItemInFridge p where p.isSold = false and p.item.id = :itemId")
    Optional<ItemInFridge> findByItemId(@Param("itemId") Integer itemId);

}
