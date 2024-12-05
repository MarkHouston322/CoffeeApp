package CoffeeApp.storageservice.services.itemService;

import CoffeeApp.storageservice.models.item.ItemInAcceptance;
import CoffeeApp.storageservice.repositories.itemRepository.ItemInAcceptanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemInAcceptanceService {

    private final ItemInAcceptanceRepository itemInAcceptanceRepository;

    @Transactional
    public void addItemInAcceptance(ItemInAcceptance itemInAcceptance){
        itemInAcceptanceRepository.save(itemInAcceptance);
    }
}
