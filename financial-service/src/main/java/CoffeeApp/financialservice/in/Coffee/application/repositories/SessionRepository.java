package CoffeeApp.financialservice.in.Coffee.application.repositories;

import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Query("SELECT p from Session p order by p.id desc limit 1")
    Optional<Session>  findLastSession();

    @Query("select p from Session p where p.sessionIsClosed = false")
    Optional<Session> findLastOpenSession();

}
