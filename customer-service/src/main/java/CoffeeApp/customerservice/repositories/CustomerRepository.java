package CoffeeApp.customerservice.repositories;

import CoffeeApp.customerservice.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findBySecondNameStartingWith(String secondName);
    List<Customer> findByMobileNumberEndingWith(String numbers);

    Optional<Customer> findByMobileNumber(String mobileNumber);
}
