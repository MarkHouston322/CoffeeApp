package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.models.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    @Query("select p from Salary p where p.isClosed = false")
    Optional<Salary> findLastOpenSalarySession();

    List<Salary> findByEmployee(Employee employee);
}
