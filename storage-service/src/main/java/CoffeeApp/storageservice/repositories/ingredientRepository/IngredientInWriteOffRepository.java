package CoffeeApp.storageservice.repositories.ingredientRepository;

import CoffeeApp.storageservice.models.ingredient.IngredientInWriteOff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientInWriteOffRepository extends JpaRepository<IngredientInWriteOff, Integer> {

    List<IngredientInWriteOff> findByIngredient_Id(Integer id);
}
