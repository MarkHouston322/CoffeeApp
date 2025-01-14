package CoffeeApp.sellingservice.repositories;

import CoffeeApp.sellingservice.models.PaymentMethod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class PaymentMethodRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("payment_method.sql");

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @BeforeEach
    void setUp(){
        List<PaymentMethod> methods = new ArrayList<>(Arrays.asList(
                new PaymentMethod("Cash"),
                new PaymentMethod("Card")
        ));
        paymentMethodRepository.saveAll(methods);
    }

    @AfterEach
    void clearUp(){
        paymentMethodRepository.deleteAll();
    }

    @Test
    void shouldFindPaymentMethodByName(){
        Optional<PaymentMethod> method = paymentMethodRepository.findByName("Cash");
        assertThat(method).isPresent();
    }

    @Test
    void shouldNotFindPaymentMethodByName(){
        Optional<PaymentMethod> method = paymentMethodRepository.findByName("Bitcoin");
        assertThat(method).isEmpty();
    }

    @Test
    void shouldFindPaymentMethodsByNameStartingWith(){
        List<PaymentMethod> methods = paymentMethodRepository.findByNameStartingWith("Ca");
        assertThat(methods).isNotEmpty();
    }

    @Test
    void shouldNotFindPaymentMethodsByNameStartingWith(){
        List<PaymentMethod> methods = paymentMethodRepository.findByNameStartingWith("Bi");
        assertThat(methods).isEmpty();
    }
}