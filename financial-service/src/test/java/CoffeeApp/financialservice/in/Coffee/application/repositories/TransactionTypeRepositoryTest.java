package CoffeeApp.financialservice.in.Coffee.application.repositories;

import CoffeeApp.financialservice.in.Coffee.application.models.TransactionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class TransactionTypeRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("transaction_type.sql");

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @BeforeEach
    void setUp() {
        List<TransactionType> types = Arrays.asList(
                new TransactionType("Cash"),
                new TransactionType("Card")
        );
        transactionTypeRepository.saveAll(types);
    }

    @AfterEach
    void clearUp(){
        transactionTypeRepository.deleteAll();
    }

    @Test
    void shouldReturnTypesByNameStartingWith(){
        List<TransactionType> types = transactionTypeRepository.findByNameStartingWith("Ca");
        assertThat(types).isNotEmpty();
        assertThat(types).size().isEqualTo(2);
    }

    @Test
    void shouldNotReturnTypesByNameStartingWith(){
        List<TransactionType> types = transactionTypeRepository.findByNameStartingWith("Ka");
        assertThat(types).isEmpty();
    }

    @Test
    void shouldReturnTypeByName(){
        Optional<TransactionType> optionalType = transactionTypeRepository.findByName("Card");
        assertThat(optionalType).isPresent();
        assertThat(optionalType.get().getName()).isEqualTo("Card");
    }

    @Test
    void shouldNotReturnTypeByName(){
        Optional<TransactionType> optionalType = transactionTypeRepository.findByName("Debit");
        assertThat(optionalType).isEmpty();
    }
}