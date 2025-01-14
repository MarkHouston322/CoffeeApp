package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.EmployeeMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedSessionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.SessionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.LastSessionIsOpenedException;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.repositories.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private KafkaTemplate<String, ProceedSessionMessage> sessionKafkaTemplate;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void shouldReturnAllSessionsDto() {
        // given
        Session session1 = new Session();
        Session session2 = new Session();
        List<Session> sessions = Arrays.asList(session1, session2);
        SessionDto sessionDto1 = new SessionDto();
        SessionDto sessionDto2 = new SessionDto();
        when(sessionRepository.findAll()).thenReturn(sessions);
        when(modelMapper.map(session1, SessionDto.class)).thenReturn(sessionDto1);
        when(modelMapper.map(session2, SessionDto.class)).thenReturn(sessionDto2);
        // when
        SessionResponse response = sessionService.findAll();
        // then
        assertThat(response.getDailyFinances()).containsExactly(sessionDto1, sessionDto2);
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void shouldNotReturnAllSessionsDto(){
        // given
        when(sessionRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        SessionResponse response = sessionService.findAll();
        // then
        assertThat(response.getDailyFinances()).isEmpty();
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnSessionDtoById() {
        // given
        int sessionId = 1;
        Session session = new Session();
        session.setId(sessionId);
        SessionDto sessionDto = new SessionDto();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(modelMapper.map(session, SessionDto.class)).thenReturn(sessionDto);
        // when
        SessionDto response = sessionService.findById(sessionId);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getCardPayment()).isEqualTo(session.getCardPayment());
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void shouldNotReturnSessionDtoByIdAndThrowException() {
        // given
        int sessionId = 1;
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> sessionService.findById(sessionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Session", "id", Integer.toString(sessionId));
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void shouldOpenSession() {
        // given
        String username = "user1";
        Session lastSession = new Session();
        SessionMessage sessionMessage = new SessionMessage(null,username,false);
        lastSession.setFiniteCash(1000);
        when(sessionRepository.findLastOpenSession()).thenReturn(Optional.empty());
        when(sessionRepository.findLastSession()).thenReturn(Optional.of(lastSession));
        when(modelMapper.map(any(Session.class), eq(SessionMessage.class))).thenReturn(sessionMessage);
        // when
        sessionService.openSession(username);
        // then
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionCaptor.capture());
        Session capturedSession = sessionCaptor.getValue();
        assertThat(capturedSession.getFiniteCash()).isEqualTo(1000);
        assertThat(capturedSession.getInitialCash()).isEqualTo(1000);
        assertThat(capturedSession.getSessionIsClosed()).isEqualTo(false);
        testSendSessionRabbit(capturedSession);
        testSendEmployeeRabbit(capturedSession);
    }

    @Test
    void shouldNotOpenSession(){
        // given
        String username = "user1";
        Session existedSession = new Session();
        existedSession.setSessionIsClosed(false);
        when(sessionRepository.findLastOpenSession()).thenReturn(Optional.of(existedSession));
        // when & then
        assertThatThrownBy(() -> sessionService.openSession(username))
                .isInstanceOf(LastSessionIsOpenedException.class)
                .hasMessageContaining("Last session is opened. Close it to be able to start new session");
        verify(sessionRepository, times(1)).findLastOpenSession();
    }

    @Test
    void shouldCloseSession(){
        // given
        Session openSession =  new Session();
        String employeeName = "Test";
        openSession.setSessionIsClosed(false);
        openSession.setEmployeeUsername(employeeName);
        openSession.setId(1);
        when(sessionRepository.findLastOpenSession()).thenReturn(Optional.of(openSession));
        // when
        sessionService.closeSession();
        // then
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionCaptor.capture());
        Session response = sessionCaptor.getValue();
        assertThat(response.getSessionIsClosed()).isEqualTo(true);
        testSendSessionRabbit(response);
        testSendEmployeeRabbit(response);
        testSendSessionKafka(response);
    }

    @Test
    void shouldNotCloseSession(){
        // given
        when(sessionRepository.findLastOpenSession()).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> sessionService.closeSession())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Session", "is closed", "false");
        verify(sessionRepository).findLastOpenSession();
    }

    @Test
    void shouldMakeCashInflow(){
        // given
        Session session = new Session();
        session.setCashInflow(1000);
        session.setFiniteCash(1000);
        int cashInflow = 1000;
        // when
        sessionService.cashInflow(session,cashInflow);
        // then
        assertThat(session.getCashInflow()).isEqualTo(2000);
        assertThat(session.getFiniteCash()).isEqualTo(2000);
    }

    @Test
    void shouldMakeCashWithdrawal(){
        // given
        Session session = new Session();
        session.setCashExpense(1000);
        session.setFiniteCash(1000);
        int cashWithdrawal = 1000;
        // when
        sessionService.cashWithdrawal(session,cashWithdrawal);
        // then
        assertThat(session.getCashExpense()).isEqualTo(2000);
        assertThat(session.getFiniteCash()).isEqualTo(0);
    }

    @Test
    void shouldMakeCardPayment(){
        // given
        Session session = new Session();
        session.setCardPayment(1000);
        session.setTotalMoney(1000);
        session.setOrdersQuantity(1);
        int sum = 1000;
        // when
        sessionService.cardPayment(session,sum);
        // then
        assertThat(session.getCardPayment()).isEqualTo(2000);
        assertThat(session.getTotalMoney()).isEqualTo(2000);
        assertThat(session.getOrdersQuantity()).isEqualTo(2);
    }

    @Test
    void shouldMakeCashPayment(){
        // given
        Session session = new Session();
        session.setCashPayment(1000);
        session.setTotalMoney(1000);
        session.setFiniteCash(1000);
        session.setOrdersQuantity(1);
        int sum = 1000;
        // when
        sessionService.cashPayment(session,sum);
        // then
        assertThat(session.getCashPayment()).isEqualTo(2000);
        assertThat(session.getTotalMoney()).isEqualTo(2000);
        assertThat(session.getFiniteCash()).isEqualTo(2000);
        assertThat(session.getOrdersQuantity()).isEqualTo(2);
    }


    private void testSendSessionRabbit(Session session) {
        ArgumentCaptor<SessionMessage> sessionMessageCaptor = ArgumentCaptor.forClass(SessionMessage.class);
        verify(streamBridge).send(eq("sendSession-out-0"), sessionMessageCaptor.capture());
        SessionMessage capturedSessionMessage = sessionMessageCaptor.getValue();
        assertThat(capturedSessionMessage.getEmployeeUsername()).isEqualTo(session.getEmployeeUsername());
    }

    private void testSendEmployeeRabbit(Session session) {
        ArgumentCaptor<EmployeeMessage> employeeMessageCaptor = ArgumentCaptor.forClass(EmployeeMessage.class);
        verify(streamBridge).send(eq("sendEmployee-out-0"), employeeMessageCaptor.capture());
        EmployeeMessage capturedEmployeeMessage = employeeMessageCaptor.getValue();
        assertThat(capturedEmployeeMessage.getEmployeeUsername()).isEqualTo(session.getEmployeeUsername());
    }

    private void testSendSessionKafka(Session session) {
        ArgumentCaptor<ProceedSessionMessage> sessionMessageCaptor = ArgumentCaptor.forClass( ProceedSessionMessage.class);
        verify(sessionKafkaTemplate).send(eq("sessions"), sessionMessageCaptor.capture());
        ProceedSessionMessage capturedSessionMessage = sessionMessageCaptor.getValue();
        assertThat(capturedSessionMessage.getTotalMoney()).isEqualTo(session.getTotalMoney());


    }
}