package CoffeeApp.sellingservice.repositories;

import CoffeeApp.sellingservice.models.GoodInOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GoodInOrderRepository extends JpaRepository<GoodInOrder, Integer> {

    List<GoodInOrder> findByOrder_Id(Integer id);
}
