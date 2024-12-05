package CoffeeApp.sellingservice.repositories;

import CoffeeApp.sellingservice.models.Order;
import CoffeeApp.sellingservice.projections.GoodProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

  @Query("select new CoffeeApp.sellingservice.projections.GoodProjectionImpl(i.goodName, i.quantity) from GoodInOrder i where i.order.id = :orderId")
  List<GoodProjection> findGoodsByOrderId(Integer orderId);
}
