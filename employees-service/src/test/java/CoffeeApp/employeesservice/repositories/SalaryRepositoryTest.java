package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.models.Salary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
class SalaryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("salary.sql");

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @AfterEach
    void clearUp() {
        salaryRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void shouldFindLastOpenSalarySession() {
        Employee employee = new Employee("Denis", null);
        employeeRepository.save(employee);
        Salary salary = new Salary(employee, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, false);
        salaryRepository.save(salary);
        Optional<Salary> optionalSalary = salaryRepository.findLastOpenSalarySession();
        assertThat(optionalSalary).isPresent();
    }

    @Test
    void shouldNotFindLastOpenSalarySession() {
        Employee employee = new Employee("Denis", null);
        employeeRepository.save(employee);
        Salary salary = new Salary(employee, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, true);
        salaryRepository.save(salary);
        Optional<Salary> optionalSalary = salaryRepository.findLastOpenSalarySession();
        assertThat(optionalSalary).isEmpty();
    }

    @Test
    void shouldFindSalaryByEmployee() {
        Employee employee = new Employee("Denis", null);
        employeeRepository.save(employee);
        Salary salary = new Salary(employee, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, true);
        salaryRepository.save(salary);
        List<Salary> salaries = salaryRepository.findByEmployee(employee);
        assertThat(salaries).isNotEmpty();
    }

    @Test
    void shouldNotFindSalaryByEmployee() {
        Employee employee = new Employee("Denis", null);
        employeeRepository.save(employee);
        List<Salary> salaries = salaryRepository.findByEmployee(employee);
        assertThat(salaries).isEmpty();
    }
}