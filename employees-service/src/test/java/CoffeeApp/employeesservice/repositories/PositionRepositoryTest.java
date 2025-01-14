package CoffeeApp.employeesservice.repositories;

import CoffeeApp.employeesservice.models.Position;
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
class PositionRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("position.sql");

    @Autowired
    private PositionRepository positionRepository;

    @BeforeEach
    void setUp() {
        List<Position> positions = new ArrayList<>(Arrays.asList(
                new Position("Manager",4000f,0.1f),
                new Position("Barista",3000f,0.05f)
        ));
        positionRepository.saveAll(positions);
    }

    @AfterEach
    void clearUp(){
        positionRepository.deleteAll();
    }

    @Test
    void shouldFindPositionByName(){
        Optional<Position> position = positionRepository.findByName("Manager");
        assertThat(position).isPresent();
    }

    @Test
    void shouldNotFindPositionByName(){
        Optional<Position> position = positionRepository.findByName("Waitress");
        assertThat(position).isEmpty();
    }

    @Test
    void shouldFindPositionByNameStartingWith(){
        List<Position> positions = positionRepository.findByNameStartingWith("Ma");
        assertThat(positions).isNotEmpty();
    }

    @Test
    void shouldNotFindPositionByNameStartingWith(){
        List<Position> positions = positionRepository.findByNameStartingWith("Ke");
        assertThat(positions).isEmpty();
    }

}