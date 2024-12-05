package CoffeeApp.customerservice.repositories;

import CoffeeApp.customerservice.models.LoyaltyLevel;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoyaltyLevelRepository extends JpaRepository<LoyaltyLevel, Integer> {
    List<LoyaltyLevel> findByNameStartingWith(String name);
    Optional<LoyaltyLevel> findByName(String name);
    @Query("SELECT p from LoyaltyLevel p WHERE p.edge < :userPurchases ORDER BY p.edge DESC LIMIT 1")
    LoyaltyLevel findByEdge(@Param("userPurchases") Integer userPurchases);

    @Query("SELECT p from LoyaltyLevel p WHERE p.edge = (select min (p.edge) from LoyaltyLevel p)")
    LoyaltyLevel findMin();
}
