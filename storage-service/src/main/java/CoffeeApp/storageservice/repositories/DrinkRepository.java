package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.models.Drink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DrinkRepository extends JpaRepository<Drink, Integer> {

    List<Drink> findByNameStartingWith(String query);

    Optional<Drink> findByName(String name);

    @Query("SELECT new CoffeeApp.storageservice.projections.IngredientProjectionImpl(i.ingredient.name, i.quantity) FROM IngredientInDrink i WHERE i.drink.id = :drinkId")
    List<IngredientProjection> findIngredientsByDrinkId(Integer drinkId);
}
