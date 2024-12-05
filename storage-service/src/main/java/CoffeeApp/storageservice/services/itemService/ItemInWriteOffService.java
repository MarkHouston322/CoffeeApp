package CoffeeApp.storageservice.services.itemService;

import CoffeeApp.storageservice.models.item.ItemInWriteOff;
import CoffeeApp.storageservice.repositories.itemRepository.ItemInWriteOffRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemInWriteOffService {

    private final ItemInWriteOffRepository itemInWriteOffRepository;

    @Transactional
    public void addItemInWriteOff(ItemInWriteOff itemInWriteOff){
        itemInWriteOffRepository.save(itemInWriteOff);
    }
}
