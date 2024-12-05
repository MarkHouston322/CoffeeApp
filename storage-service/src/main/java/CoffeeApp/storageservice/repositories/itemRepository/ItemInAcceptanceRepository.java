package CoffeeApp.storageservice.repositories.itemRepository;

import CoffeeApp.storageservice.models.item.ItemInAcceptance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemInAcceptanceRepository extends JpaRepository<ItemInAcceptance, Integer> {
}
