package CoffeeApp.financialservice.in.Coffee.application.repositories;

import CoffeeApp.financialservice.in.Coffee.application.models.TotalFinance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TotalFinanceRepository extends JpaRepository<TotalFinance, Integer> {
}
