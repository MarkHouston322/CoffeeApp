package CoffeeApp.storageservice.services.ingredientService;

import CoffeeApp.storageservice.models.ingredient.IngredientInAcceptance;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientInAcceptanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class IngredientInAcceptanceService {

    private final IngredientInAcceptanceRepository ingredientInAcceptanceRepository;

    @Transactional
    public void addIngredientInAcceptance(IngredientInAcceptance ingredientInAcceptance){
        ingredientInAcceptanceRepository.save(ingredientInAcceptance);
    }
}
