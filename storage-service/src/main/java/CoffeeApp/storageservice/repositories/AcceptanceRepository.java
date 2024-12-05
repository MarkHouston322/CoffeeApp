package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.models.Acceptance;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcceptanceRepository extends JpaRepository<Acceptance, Integer> {

    @Query("SELECT new CoffeeApp.storageservice.projections.IngredientProjectionImpl(i.ingredient.name, i.quantity) FROM IngredientInAcceptance i WHERE i.acceptance.id = :acceptanceId")
    List<IngredientProjection> findIngredientsByAcceptanceId(Integer acceptanceId);

    @Query("SELECT new CoffeeApp.storageservice.projections.ItemProjectionImpl(i.item.name, i.quantity, i.item.costPrice) from ItemInAcceptance i where i.acceptance.id = :acceptanceId")
    List<ItemProjection> findItemsByAcceptanceId(Integer acceptanceId);
}
