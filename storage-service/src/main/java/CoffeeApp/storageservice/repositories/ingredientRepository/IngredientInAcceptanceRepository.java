package CoffeeApp.storageservice.repositories.ingredientRepository;

import CoffeeApp.storageservice.models.ingredient.IngredientInAcceptance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientInAcceptanceRepository extends JpaRepository<IngredientInAcceptance, Integer> {

    List<IngredientInAcceptance> findByIngredient_Id(Integer id);
}
