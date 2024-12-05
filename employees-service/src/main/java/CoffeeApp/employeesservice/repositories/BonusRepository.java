package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BonusRepository extends JpaRepository<Bonus, Integer> {

    Optional<Bonus> findByEdge(Integer edge);

    @Query("select p from Bonus p where p.edge < :revenue order by p.edge desc limit 1")
    Optional<Bonus> findByRequiredEdge(@Param("revenue") Integer revenue);
}
