package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Testcontainers
class EmployeeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("bonus.sql");

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        Employee employee = new Employee();
        employee.setName("Test");
        employeeRepository.save(employee);
    }

    @AfterEach
    void clearUp(){
        employeeRepository.deleteAll();
    }

    @Test
    void shouldFindEmployeeByNameStartingWith(){
        List<Employee> employeeList = employeeRepository.findByNameStartingWith("Te");
        assertThat(employeeList).doesNotContainNull();
    }

    @Test
    void shouldNotFindEmployeeByNameStartingWith(){
        List<Employee> employeeList = employeeRepository.findByNameStartingWith("Vs");
        assertThat(employeeList).isEmpty();
    }

    @Test
    void shouldFindEmployeeByName(){
        Optional<Employee> employee = employeeRepository.findByName("Test");
        assertThat(employee).isNotEmpty();
    }

    @Test
    void shouldNotFindEmployeeByName(){
        Optional<Employee> employee = employeeRepository.findByName("Example");
        assertThat(employee).isEmpty();
    }

    @Test
    void shouldDeleteEmployeeByName(){
        employeeRepository.deleteByName("Test");
        assertThat(employeeRepository.findByName("Test")).isEmpty();
    }

    @Test
    void shouldNotDeleteEmployeeByName(){
       assertThatThrownBy(() -> employeeRepository.deleteByName("Example"))
               .isInstanceOf(ResourceNotFoundException.class);
    }


}