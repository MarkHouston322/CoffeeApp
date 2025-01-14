package CoffeeApp.financialservice.in.Coffee.application.repositories;

import CoffeeApp.financialservice.in.Coffee.application.models.Transaction;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class TransactionRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("transaction.sql");

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @BeforeEach
    void setUp() {
        TransactionType transactionType = new TransactionType("Cash");
        transactionTypeRepository.save(transactionType);
        List<Transaction> transactions = Arrays.asList(
                new Transaction(transactionType,1000),
                new Transaction(transactionType,2000)
        );
        transactionRepository.saveAll(transactions);
    }

    @AfterEach
    void clearUp(){
        transactionTypeRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void shouldFindByTypeName(){
        List<Transaction> transactions = transactionRepository.findByTypeName("Cash");
        assertThat(transactions).isNotEmpty();
    }

    @Test
    void shouldNotFindByTypeName(){
        List<Transaction> transactions = transactionRepository.findByTypeName("Card");
        assertThat(transactions).isEmpty();
    }

}