package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.models.WriteOff;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WriteOffRepository extends JpaRepository<WriteOff,Integer> {

    @Query("SELECT new CoffeeApp.storageservice.projections.IngredientProjectionImpl(i.ingredient.name, i.quantity) FROM IngredientInWriteOff i WHERE i.writeOff.id = :writeOffId")
    List<IngredientProjection> findIngredientsByWriteOffId(Integer writeOffId);

    @Query("SELECT new CoffeeApp.storageservice.projections.ItemProjectionImpl(i.item.name, i.quantity, i.item.costPrice) FROM ItemInWriteOff i WHERE i.writeOff.id = :writeOffId")
    List<ItemProjection> findItemsByWriteOffId(Integer writeOffId);
}
