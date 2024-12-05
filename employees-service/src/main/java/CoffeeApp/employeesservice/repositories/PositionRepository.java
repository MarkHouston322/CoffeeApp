package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findByName(String name);
    List<Position> findByNameStartingWith(String name);
}
