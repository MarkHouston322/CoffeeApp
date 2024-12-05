package CoffeeApp.storageservice.repositories.itemRepository;

import CoffeeApp.storageservice.models.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByNameStartingWith(String name);

    Optional<Item> findByName(String name);


}
