package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.employeeDto.EmployeeDto;
import CoffeeApp.employeesservice.dto.employeeDto.EmployeeResponse;
import CoffeeApp.employeesservice.exceptions.EmployeeAlreadyExistsException;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.repositories.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PositionService positionService;

    @InjectMocks
    private EmployeeService employeeService;

    private final Position position = new Position();

    @Test
    void shouldReturnEmployeeByName() {
        // given
        String name = "Test";
        Employee employee = new Employee(name, position);
        when(employeeRepository.findByName(name)).thenReturn(Optional.of(employee));
        // when
        Employee result = employeeService.findByName(name);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(employee.getName());
        verify(employeeRepository).findByName(name);
    }

    @Test
    void shouldNotReturnEmployeeDtoByName() {
        // given
        String name = "Test";
        when(employeeRepository.findByName(name)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> employeeService.findByName(name))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee", "name", name);
        verify(employeeRepository).findByName(name);
    }

    @Test
    void shouldReturnAllEmployees() {
        // given
        Employee employee1 = new Employee("Denis", position);
        Employee employee2 = new Employee("Nikita", position);
        List<Employee> employees = Arrays.asList(employee1, employee2);
        EmployeeDto employeeDto1 = new EmployeeDto("Denis", position);
        EmployeeDto employeeDto2 = new EmployeeDto("Nikita", position);

        when(employeeRepository.findAll()).thenReturn(employees);
        when(modelMapper.map(employee1, EmployeeDto.class)).thenReturn(employeeDto1);
        when(modelMapper.map(employee2, EmployeeDto.class)).thenReturn(employeeDto2);
        // when
        EmployeeResponse response = employeeService.findAll();
        // then
        assertThat(response.getEmployees()).containsExactly(employeeDto1, employeeDto2);
        verify(employeeRepository).findAll();
    }

    @Test
    void shouldNotReturnAllEmployees() {
        // given
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        EmployeeResponse response = employeeService.findAll();
        // then
        assertThat(response.getEmployees()).isEmpty();
        verify(employeeRepository).findAll();
    }

    @Test
    void shouldAddEmployee() {
        // given
        String employeeName = "Test";
        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.empty());
        when(positionService.findById(Mockito.anyInt())).thenReturn(position);
        Employee savedEmployee = new Employee(employeeName, position);
        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        // when
        Employee result = employeeService.addEmployee(employeeName);
        // then
        ArgumentCaptor<Employee> argumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(argumentCaptor.capture());
        Employee capturedEmployee = argumentCaptor.getValue();

        assertThat(capturedEmployee.getName()).isEqualTo(employeeName);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(employeeName);
    }

    @Test
    void shouldNotAddEmployee(){
        // given
        String employeeName = "Test";
        Employee existingEmployee = new Employee(employeeName,position);
        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.of(existingEmployee));
        // when
        assertThatThrownBy(() -> employeeService.addEmployee(employeeName))
                .isInstanceOf(EmployeeAlreadyExistsException.class)
                .hasMessageContaining("Employee has already been added with this name");
    }

    @Test
    void shouldDeleteEmployeeById(){
        // given
        String employeeName = "Test";
        Employee employee = new Employee(employeeName,position);
        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteByName(employeeName);
        // when
        employeeService.deleteEmployee(employeeName);
        // then
        verify(employeeRepository).findByName(employeeName);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(employeeRepository).deleteByName(stringCaptor.capture());
        assertThat(stringCaptor.getValue()).isEqualTo(employeeName);
    }

    @Test
    void shouldAssignPosition(){
        // given
        String employeeName = "Test";
        Employee employee = new Employee(employeeName,null);
        int positionId = 1;
        position.setId(positionId);
        when(employeeRepository.findByName(employeeName)).thenReturn(Optional.of(employee));
        when(positionService.findById(positionId)).thenReturn(position);
        // when
        employeeService.assignPosition(employeeName,positionId);
        // then
        assertThat(employee.getPosition()).isEqualTo(position);
        verify(employeeRepository).findByName(employeeName);
        verify(positionService).findById(positionId);
    }
}