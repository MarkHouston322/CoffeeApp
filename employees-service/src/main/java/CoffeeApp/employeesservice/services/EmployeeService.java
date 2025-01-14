package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.employeeDto.EmployeeDto;
import CoffeeApp.employeesservice.dto.employeeDto.EmployeeResponse;
import CoffeeApp.employeesservice.dto.positionDto.PositionDto;
import CoffeeApp.employeesservice.exceptions.EmployeeAlreadyExistsException;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.repositories.EmployeeRepository;
import CoffeeApp.employeesservice.repositories.PositionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionService positionService;
    private final ModelMapper modelMapper;

    @Transactional(noRollbackFor = ResourceNotFoundException.class)
    public Employee findByName(String name) {
        return checkIfExists(name);
    }

    public EmployeeResponse findAll() {
        return new EmployeeResponse(employeeRepository.findAll().stream().map(this::convertToEmployeeDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public Employee addEmployee(String employeeName) {
        Optional<Employee> optionalEmployee = employeeRepository.findByName(employeeName);
        if (optionalEmployee.isPresent()){
            throw new EmployeeAlreadyExistsException("Employee has already been added with this name: " + employeeName);
        }
        Employee employeeToAdd = new Employee(employeeName, positionService.findById(1));
        employeeRepository.save(employeeToAdd);
        return employeeToAdd;
    }


    @Transactional
    public void deleteEmployee(String name) {
        checkIfExists(name);
        employeeRepository.deleteByName(name);
    }

    @Transactional
    public void assignPosition(String employeeName, Integer positionId) {
        Employee employee = checkIfExists(employeeName);
        Position position = positionService.findById(positionId);
        employee.setPosition(position);
    }

    private Employee checkIfExists(String name) {
        return employeeRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", name)
        );
    }

    private EmployeeDto convertToEmployeeDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

}
