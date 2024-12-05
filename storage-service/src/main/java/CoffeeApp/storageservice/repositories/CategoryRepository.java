package CoffeeApp.storageservice.repositories;

import CoffeeApp.storageservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByNameStartingWith(String query);
    Optional<Category> findByName(String name);
}
