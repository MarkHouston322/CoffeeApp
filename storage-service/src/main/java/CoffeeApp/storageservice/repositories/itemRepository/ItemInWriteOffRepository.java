package CoffeeApp.storageservice.repositories.itemRepository;

import CoffeeApp.storageservice.models.item.ItemInWriteOff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInWriteOffRepository extends JpaRepository<ItemInWriteOff, Integer> {
}
