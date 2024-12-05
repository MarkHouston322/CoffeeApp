package CoffeeApp.storageservice.repositories.ingredientRepository;

import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientInDrinkRepository extends JpaRepository<IngredientInDrink, Integer> {

    List<IngredientInDrink> findByDrink_Id (Integer id);
    void deleteByDrink_Id(Integer id);
}
