package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByNameStartingWith(String name);
    Optional<Employee> findByName(String name);
    void deleteByName(String name);
}
