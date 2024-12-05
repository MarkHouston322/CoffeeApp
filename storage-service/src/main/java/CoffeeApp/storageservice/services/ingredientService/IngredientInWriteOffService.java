package CoffeeApp.storageservice.services.ingredientService;

import CoffeeApp.storageservice.models.ingredient.IngredientInWriteOff;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientInWriteOffRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class IngredientInWriteOffService {

    private final IngredientInWriteOffRepository ingredientInWriteOffRepository;

    @Transactional
    public void addIngredientInWriteOff(IngredientInWriteOff ingredientInWriteOff){
        ingredientInWriteOffRepository.save(ingredientInWriteOff);
    }
}
