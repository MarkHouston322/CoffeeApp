package CoffeeApp.financialservice.in.Coffee.application.repositories;

import CoffeeApp.financialservice.in.Coffee.application.models.Session;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class SessionRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("session.sql");

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        List<Session> sessions = new ArrayList<>(Arrays.asList(
                new Session(1, LocalDateTime.now(),"admin",0,0,0,0,0,0,0,0,LocalDateTime.now(),true,null),
                new Session(1, LocalDateTime.now(),"admin",0,0,0,0,0,0,0,0,LocalDateTime.now(),false,null)
        ));
        sessionRepository.saveAll(sessions);
    }

    @AfterEach
    void clearUp(){
        sessionRepository.deleteAll();
    }

    @Test
    void shouldFindLastSession(){
        Optional<Session> session = sessionRepository.findLastSession();
        assertThat(session).isPresent();
    }

    @Test
    void shouldNotFindLastSession(){
        sessionRepository.deleteAll();
        Optional<Session> session = sessionRepository.findLastSession();
        assertThat(session).isEmpty();
    }

    @Test
    void shouldFindLastOpenedSession(){
        Optional<Session> session = sessionRepository.findLastOpenSession();
        assertThat(session).isPresent();
    }

    @Test
    void shouldNotFindLastOpenedSession(){
        sessionRepository.deleteAll();
        Optional<Session> session = sessionRepository.findLastOpenSession();
        assertThat(session).isEmpty();
    }

}