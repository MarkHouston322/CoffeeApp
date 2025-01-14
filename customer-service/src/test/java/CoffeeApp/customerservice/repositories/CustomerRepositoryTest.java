package CoffeeApp.customerservice.repositories;

import CoffeeApp.customerservice.models.Customer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class CustomerRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("customer.sql");

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "9998117351",
                "test111@gmail.com"
        );
        customerRepository.save(customer);
    }

    @AfterEach
    void clearUp(){
        customerRepository.deleteAll();
    }

    @Test
    void shouldFindCustomerBySecondNameStartingWith() {
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("Secon");
        assertThat(customerList).doesNotContainNull();
    }

    @Test
    void shouldNotFindCustomerBySecondNameStartingWith() {
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("ytyt");
        assertThat(customerList).isEmpty();
    }

    @Test
    void shouldFindCustomerByMobileNUmberEndingWith() {
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("7351");
        assertThat(customerList).doesNotContainNull();
    }

    @Test
    void shouldNotFindCustomerByMobileNUmberEndingWith() {
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("5555");
        assertThat(customerList).isEmpty();
    }

    @Test
    void shouldFindCustomerByMobileNumber(){
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber("9998117351");
        assertThat(optionalCustomer).isNotEmpty();
    }

    @Test
    void shouldNotFindCustomerByMobileNumber(){
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber("9988117351");
        assertThat(optionalCustomer).isEmpty();
    }
}