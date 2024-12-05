package CoffeeApp.sellingservice.repositories;

import CoffeeApp.sellingservice.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

    Optional<PaymentMethod> findByName(String name);

    List<PaymentMethod> findByNameStartingWith(String name);
}
