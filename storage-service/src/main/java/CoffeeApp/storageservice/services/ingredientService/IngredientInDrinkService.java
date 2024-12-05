package CoffeeApp.storageservice.services.ingredientService;

import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientInDrinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class IngredientInDrinkService {

    private final IngredientInDrinkRepository ingredientInDrinkRepository;

    public List<IngredientInDrink> findByDrink_Id(Integer id){
        return ingredientInDrinkRepository.findByDrink_Id(id);
    }

    @Transactional
    public void addIngredientInDrink (IngredientInDrink ingredientInDrink){
        ingredientInDrinkRepository.save(ingredientInDrink);
    }

    @Transactional
    public void deleteIngredientInDrink(Integer drinkId){
        ingredientInDrinkRepository.deleteByDrink_Id(drinkId);
    }

}
