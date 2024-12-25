package CoffeeApp.financialservice.in.Coffee.application.repositories;

import CoffeeApp.financialservice.in.Coffee.application.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByTypeName(String name);
}
