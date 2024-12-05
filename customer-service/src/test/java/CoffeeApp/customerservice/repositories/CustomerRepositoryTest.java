package CoffeeApp.customerservice.repositories;

import CoffeeApp.customerservice.models.Customer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void shouldFindCustomerBySecondNameStartingWith() {
        // give
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "9998117351",
                "test111@gmail.com"
        );
        customerRepository.save(customer);
        // when
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("Secon");
        // then
        assertThat(customerList).doesNotContainNull();
    }

    @Test
    void shouldNotFindCustomerBySecondNameStartingWith() {
        // give
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "8888117351",
                "test222@gmail.com"
        );
        customerRepository.save(customer);
        // when
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("ytyt");
        // then
        assertThat(customerList).isEmpty();
    }

    @Test
    void shouldFindCustomerByMobileNUmberEndingWith() {
        // give
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "7778117351",
                "test333@gmail.com"
        );
        customerRepository.save(customer);
        // when
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("7351");
        // then
        assertThat(customerList).doesNotContainNull();
    }

    @Test
    void shouldNotFindCustomerByMobileNUmberEndingWith() {
        // give
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "6668117351",
                "test444@gmail.com"
        );
        customerRepository.save(customer);
        // when
        List<Customer> customerList = customerRepository.findBySecondNameStartingWith("5555");
        // then
        assertThat(customerList).isEmpty();
    }

    @Test
    void shouldFindCustomerByMobileNumber(){
        // give
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "5558117351",
                "test555@gmail.com"
        );
        customerRepository.save(customer);
        // when
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber("5558117351");
        // then
        assertThat(optionalCustomer).isNotEmpty();
    }

    @Test
    void shouldNotFindCustomerByMobileNumber(){
        // give
        Customer customer = new Customer(
                "Name",
                "SecondName",
                "444117351",
                "test666@gmail.com"
        );
        customerRepository.save(customer);
        // when
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber("9988117351");
        // then
        assertThat(optionalCustomer).isEmpty();
    }


}