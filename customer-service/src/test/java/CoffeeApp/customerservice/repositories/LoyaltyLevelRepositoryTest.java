package CoffeeApp.customerservice.repositories;

import CoffeeApp.customerservice.models.LoyaltyLevel;
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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
class LoyaltyLevelRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("customer.sql");

    @Autowired
    private LoyaltyLevelRepository loyaltyLevelRepository;

    // given
    @BeforeEach
    void setUp(){
        LoyaltyLevel loyaltyLevel = new LoyaltyLevel("PRO",1000,0.15f);
        loyaltyLevelRepository.save(loyaltyLevel);
    }

    @AfterEach
    void clearUp(){
        loyaltyLevelRepository.deleteAll();
    }


    @Test
    void shouldFindLoyaltyLevelByNameStartingWith(){
        // when
        List<LoyaltyLevel> loyaltyLevelList = loyaltyLevelRepository.findByNameStartingWith("PR");
        // then
        assertThat(loyaltyLevelList).isNotEmpty();
    }

    @Test
    void shouldNotFindLoyaltyLevelByNameStartingWith(){
        // when
        List<LoyaltyLevel> loyaltyLevelList = loyaltyLevelRepository.findByNameStartingWith("ss");
        // then
        assertThat(loyaltyLevelList).isEmpty();
    }

    @Test
    void shouldFindLoyaltyLevelByName(){
        // when
        Optional<LoyaltyLevel> optionalLoyaltyLevel = loyaltyLevelRepository.findByName("PRO");
        // then
        assertThat(optionalLoyaltyLevel).isPresent();
    }

    @Test
    void shouldNotFindLoyaltyLevelByName(){
        // when
        Optional<LoyaltyLevel> optionalLoyaltyLevel = loyaltyLevelRepository.findByName("ss");
        // then
        assertThat(optionalLoyaltyLevel).isEmpty();
    }

    @Test
    void shouldFindLoyaltyLevelWithMinEdge(){
        // given
        LoyaltyLevel loyaltyLevel1 = new LoyaltyLevel("Noob",2000,0.15f);
        LoyaltyLevel loyaltyLevel2 = new LoyaltyLevel("Middle",3000,0.15f);
        loyaltyLevelRepository.save(loyaltyLevel1);
        loyaltyLevelRepository.save(loyaltyLevel2);
        // when
        LoyaltyLevel loyaltyLevel = loyaltyLevelRepository.findMin();
        // then
        assertThat(loyaltyLevel.getEdge()).isEqualTo(1000);
    }

    @Test
    void shouldFindLoyaltyLevelByCustomerPurchasesSum(){
        LoyaltyLevel loyaltyLevel1 = new LoyaltyLevel("Noob",2000,0.15f);
        LoyaltyLevel loyaltyLevel2 = new LoyaltyLevel("Middle",3000,0.15f);
        loyaltyLevelRepository.save(loyaltyLevel1);
        loyaltyLevelRepository.save(loyaltyLevel2);
        // when
        LoyaltyLevel loyaltyLevel = loyaltyLevelRepository.findByEdge(2550);
        // then
        assertThat(loyaltyLevel.getEdge()).isEqualTo(2000);
    }
}