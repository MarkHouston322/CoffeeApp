package CoffeeApp.storageservice.repositories.ingredientRepository;

import CoffeeApp.storageservice.models.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    List<Ingredient> findByNameStartingWith(String query);
    Optional<Ingredient> findByName(String name);
}
