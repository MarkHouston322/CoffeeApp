package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.messages.OrderMessage;
import CoffeeApp.employeesservice.dto.messages.ProceedSalaryMessage;
import CoffeeApp.employeesservice.dto.messages.SessionMessage;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryDto;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryResponse;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Bonus;
import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.models.Salary;
import CoffeeApp.employeesservice.repositories.EmployeeRepository;
import CoffeeApp.employeesservice.repositories.SalaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryServiceTest {

    @Mock
    private SalaryRepository salaryRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BonusService bonusService;

    @Mock
    private KafkaTemplate<String, ProceedSalaryMessage> salaryKafkaTemplate;

    @Spy
    @InjectMocks
    private SalaryService salaryService;

    private final Position position = new Position("Barista",3000f,0.05f);

    private final Employee employee1 = new Employee("Denis",position);
    private final Employee employee2 = new Employee("Nikita",position);


    @Test
    void shouldReturnSalaryDtoById(){
        // given
        int id = 1;
        Salary salary = new Salary(null, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, true);
        salary.setId(id);
        SalaryDto salaryDto = new SalaryDto(null, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, true);
        when(salaryRepository.findById(id)).thenReturn(Optional.of(salary));
        when(modelMapper.map(salary, SalaryDto.class)).thenReturn(salaryDto);
        // when
        SalaryDto result = salaryService.findById(id);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(salary.getTotal());
        assertThat(result.getRevenue()).isEqualTo(salary.getRevenue());
        verify(salaryRepository).findById(id);
    }

    @Test
    void shouldNotReturnSalaryDtoById() {
        // given
        int id = 1;
        when(salaryRepository.findById(id)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> salaryService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Salary", "id", Integer.toString(id));
        verify(salaryRepository).findById(id);
    }

    @Test
    void shouldReturnCurrentSalarySession(){
        // given
        Salary salary = new Salary(null, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, false);
        when(salaryRepository.findLastOpenSalarySession()).thenReturn(Optional.of(salary));
        // when
        Salary result = salaryService.findCurrentSalarySession();
        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(salary.getTotal());
        assertThat(result.getIsClosed()).isEqualTo(false);
    }

    @Test
    void shouldNotReturnCurrentSalarySession(){
        // given
        when(salaryRepository.findLastOpenSalarySession()).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> salaryService.findCurrentSalarySession())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Salary session", "is closed", "false");
        verify(salaryRepository).findLastOpenSalarySession();
    }

    @Test
    void shouldReturnSalariesDtoByEmployee(){
        // given
        Salary salary1 = new Salary(employee1, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, false);
        Salary salary2 = new Salary(employee1, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, true);
        List<Salary> salaries = Arrays.asList(salary1,salary2);
        SalaryDto salaryDto1 = new SalaryDto(employee1, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, false);
        SalaryDto salaryDto2 = new SalaryDto(employee1, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 15000, true);
        when(employeeService.findByName(employee1.getName())).thenReturn(employee1);
        when(salaryRepository.findByEmployee(employee1)).thenReturn(salaries);
        when(modelMapper.map(salary1, SalaryDto.class)).thenReturn(salaryDto1);
        when(modelMapper.map(salary2, SalaryDto.class)).thenReturn(salaryDto2);
        // when
        SalaryResponse response = salaryService.findByEmployeeName(employee1.getName());
        // then
        assertThat(response.getSalaries()).containsExactly(salaryDto1,salaryDto2);
        verify(salaryRepository).findByEmployee(employee1);
        verify(employeeService).findByName(employee1.getName());
    }

    @Test
    void shouldNotReturnSalariesDtoByEmployee(){
        // given
        when(salaryRepository.findByEmployee(Mockito.any())).thenReturn(Collections.emptyList());
        // when
        SalaryResponse response = salaryService.findByEmployeeName(Mockito.anyString());
        // then
        assertThat(response.getSalaries()).isEmpty();
        verify(salaryRepository).findByEmployee(Mockito.any());
    }

    @Test
    void shouldOpenSalarySessionForExistingEmployee(){
        // given
        when(employeeService.findByName(Mockito.anyString())).thenReturn(employee1);
        SessionMessage sessionMessage = new SessionMessage("Denis",false);
        // when
        salaryService.openSalarySession(sessionMessage);
        // then
        ArgumentCaptor<Salary> sessionCaptor = ArgumentCaptor.forClass(Salary.class);
        verify(salaryRepository).save(sessionCaptor.capture());
        Salary result = sessionCaptor.getValue();
        assertThat(result.getEmployee()).isEqualTo(employee1);
        assertThat(result.getSalary()).isEqualTo(position.getSalary());
        assertThat(result.getRoyalty()).isEqualTo(position.getRoyalty());
        assertThat(result.getIsClosed()).isEqualTo(false);
    }

    @Test
    void shouldOpenSalarySessionForNewEmployee(){
        // given
        SessionMessage sessionMessage = new SessionMessage("Denis",false);
        when(employeeService.findByName(employee1.getName()))
                .thenThrow(new ResourceNotFoundException("Employee", "name", employee1.getName()));
        when(employeeService.addEmployee(sessionMessage.getEmployeeUsername())).thenReturn(employee1);
        // when
        salaryService.openSalarySession(sessionMessage);
        // then
        ArgumentCaptor<Salary> salaryCaptor = ArgumentCaptor.forClass(Salary.class);
        verify(salaryRepository).save(salaryCaptor.capture());
        Salary result = salaryCaptor.getValue();
        assertThat(result.getEmployee()).isEqualTo(employee1);
        assertThat(result.getSalary()).isEqualTo(position.getSalary());
        assertThat(result.getRoyalty()).isEqualTo(position.getRoyalty());
        assertThat(result.getIsClosed()).isEqualTo(false);
    }

    @Test
    void shouldCloseSalarySession(){
        // given
        Salary salary = new Salary(employee1, LocalDateTime.now(), 3000f, 0.05f, 500, 5000f, 1000, false);
        doReturn(salary).when(salaryService).findCurrentSalarySession();
        // when
        salaryService.closeSalarySession();
        // then
        assertThat(salary.getIsClosed()).isEqualTo(true);
        ArgumentCaptor<ProceedSalaryMessage> messageCaptor = ArgumentCaptor.forClass(ProceedSalaryMessage.class);
        verify(salaryKafkaTemplate).send(eq("salaries"), messageCaptor.capture());
        ProceedSalaryMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage).isNotNull();
        assertThat(sentMessage.getDate()).isNotNull();
        assertThat(sentMessage.getEmployeeName()).isEqualTo(employee1.getName());
        assertThat(sentMessage.getTotal()).isEqualTo(salary.getTotal());
    }

    @Test
    void shouldUpdateCurrentSalarySessionForSameEmployee(){
        // given
        Salary currentSession = new Salary(employee1, LocalDateTime.now(), 5000f, 0.05f, 0, 5000f, 1000, false);
        OrderMessage orderMessage = new OrderMessage(employee1.getName(), 2000);
        Bonus bonus = new Bonus(3000,1000);
        doReturn(currentSession).when(salaryService).findCurrentSalarySession();
        when(bonusService.findByRequiredEdge(3000)).thenReturn(bonus);
        // when
        salaryService.updateSalary(orderMessage);
        // then
        assertThat(currentSession.getRevenue()).isEqualTo(3000);
        assertThat(currentSession.getBonus()).isEqualTo(1000);
        assertThat(currentSession.getTotal()).isEqualTo(currentSession.getSalary() + (0.05f * 3000) + 1000);
        verify(salaryService, never()).closeSalarySession();
    }

    @Test
    void shouldCloseAndOpenNewSalarySessionForDifferentEmployee(){
        // given
        Salary currentSession = new Salary(employee1, LocalDateTime.now(), 5000f, 0.05f, 0, 5000f, 1000, false);
        OrderMessage orderMessage = new OrderMessage("Nikita", 2000);
        Bonus bonus = new Bonus(3000,1000);
        doReturn(currentSession).when(salaryService).findCurrentSalarySession();
        when(bonusService.findByRequiredEdge(anyInt())).thenReturn(bonus);
        // when
        salaryService.updateSalary(orderMessage);
        // then
        verify(salaryService).closeSalarySession();
        verify(salaryService).openSalarySession(argThat(sessionMessage ->
                sessionMessage.getEmployeeUsername().equals(orderMessage.getEmployeeName())
                        && !sessionMessage.getIsClosed()));
        assertThat(currentSession.getRevenue()).isEqualTo(0);

    }

}