package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Bonus;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class BonusRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("bonus.sql");

    @Autowired
    private BonusRepository bonusRepository;

    @BeforeEach
    void setUp() {
        List<Bonus> bonuses = new ArrayList<>(Arrays.asList(
                new Bonus(1000, 500),
                new Bonus(2000, 1000),
                new Bonus(3000, 1500)
        ));
        bonusRepository.saveAll(bonuses);
    }

    @AfterEach
    void clearUp() {
        bonusRepository.deleteAll();
    }

    @Test
    void shouldFindBonusByEdge() {
        Optional<Bonus> optionalBonus = bonusRepository.findByEdge(1000);
        assertThat(optionalBonus).isPresent();
    }

    @Test
    void shouldNotFindBonusByEdge() {
        Optional<Bonus> optionalBonus = bonusRepository.findByEdge(1500);
        assertThat(optionalBonus).isEmpty();
    }

    @Test
    void shouldFindBonusByEmployeeRevenue() {
        Optional<Bonus> bonus = bonusRepository.findByRequiredEdge(2500);
        assertThat(bonus).isPresent();
        assertThat(bonus.get().getEdge()).isEqualTo(2000);
    }

}