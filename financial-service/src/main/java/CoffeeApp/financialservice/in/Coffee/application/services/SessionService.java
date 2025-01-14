package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.EmployeeMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedSessionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionResponse;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.SessionMessage;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.LastSessionIsOpenedException;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.repositories.SessionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ModelMapper modelMapper;
    private final StreamBridge streamBridge;
    private final KafkaTemplate<String, ProceedSessionMessage> sessionKafkaTemplate;


    public SessionResponse findAll() {
        return new SessionResponse(sessionRepository.findAll().stream().map(this::convertSessionDto)
                .collect(Collectors.toList()));
    }

    public SessionDto findById(Integer id) {
        Session session = checkIfExists(id);
        return convertSessionDto(session);
    }

    @Transactional
    public void openSession(String username) {
        findLastOpenSessionIfExists();
        Session session = new Session();
        Optional<Session> lastSession = sessionRepository.findLastSession();
        int initialCash = lastSession.map(Session::getFiniteCash).orElse(0);
        session.setOpeningDate(LocalDateTime.now());
        session.setEmployeeUsername(username);
        session.setCashInflow(0);
        session.setCashExpense(0);
        session.setCardPayment(0);
        session.setCashPayment(0);
        session.setOrdersQuantity(0);
        session.setTotalMoney(0);
        session.setSessionIsClosed(false);
        session.setInitialCash(initialCash);
        session.setFiniteCash(initialCash);
        sessionRepository.save(session);
        sendSession(session);
        sendEmployee(session);
    }

    @Transactional
    public void closeSession() {
        Session openSession = findCurrentSession();
        openSession.setId(openSession.getId());
        openSession.setTransactions(openSession.getTransactions());
        openSession.setClosingDate(LocalDateTime.now());
        openSession.setSessionIsClosed(true);
        sessionRepository.save(openSession);
        sendSession(openSession);
        sendEmployee(openSession);
        sendSessionMessage(openSession,sessionKafkaTemplate);
    }

    private void findLastOpenSessionIfExists() {
        Optional<Session> dailyFinance = sessionRepository.findLastOpenSession();
        if (dailyFinance.isPresent()) {
            throw new LastSessionIsOpenedException("Last session is opened. Close it to be able to start new session");
        }
    }

    public Session findCurrentSession() {
        return sessionRepository.findLastOpenSession().orElseThrow(
                () -> new ResourceNotFoundException("Session", "is closed", "false")
        );
    }

    public void cashInflow(Session session, Integer sum) {
        session.setCashInflow(session.getCashInflow() + sum);
        session.setFiniteCash(session.getFiniteCash() + sum);
    }

    public void cashWithdrawal(Session session, Integer sum) {
        session.setCashExpense(session.getCashExpense() + sum);
        session.setFiniteCash(session.getFiniteCash() - sum);
    }

    public void cardPayment(Session session, Integer sum) {
        session.setCardPayment(session.getCardPayment() + sum);
        session.setTotalMoney(session.getTotalMoney() + sum);
        session.setOrdersQuantity(session.getOrdersQuantity() + 1);
    }

    public void cashPayment(Session session, Integer sum) {
        session.setCashPayment(session.getCashPayment() + sum);
        session.setFiniteCash(session.getFiniteCash() + sum);
        session.setTotalMoney(session.getTotalMoney() + sum);
        session.setOrdersQuantity(session.getOrdersQuantity() + 1);
    }


    private void sendSession(Session session) {
        SessionMessage sessionMessage = convertToSessionMessage(session);
        streamBridge.send("sendSession-out-0", sessionMessage);
    }

    private void sendEmployee(Session session) {
        EmployeeMessage employeeMessage = new EmployeeMessage(session.getEmployeeUsername(), session.getSessionIsClosed());
        streamBridge.send("sendEmployee-out-0", employeeMessage);
    }

    private void sendSessionMessage(Session session, KafkaTemplate<String,ProceedSessionMessage> sessionKafkaTemplate){
        ProceedSessionMessage proceedSessionMessage = new ProceedSessionMessage(LocalDateTime.now(),session.getInitialCash());
        sessionKafkaTemplate.send("sessions",proceedSessionMessage);
    }


    private Session checkIfExists(int id) {
        return sessionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Drink", "id", Integer.toString(id))
        );
    }

    private SessionDto convertSessionDto(Session session) {
        return modelMapper.map(session, SessionDto.class);
    }

    private SessionMessage convertToSessionMessage(Session session) {
        return modelMapper.map(session, SessionMessage.class);
    }

}
