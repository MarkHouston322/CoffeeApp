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
    private final PositionRepository positionRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);


    // конвертить в дто в контроллере
    public EmployeeResponse findByNameStartingWith(String name) {
        return new EmployeeResponse(employeeRepository.findByNameStartingWith(name).stream().map(this::convertToEmployeeDto)
                .collect(Collectors.toList()));
    }

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
        try {
            Employee employeeToAdd = new Employee(employeeName, positionService.findById(1));
            employeeRepository.save(employeeToAdd);
            return employeeToAdd;
        } catch (Exception e) {
            throw e;
        }
    }


    @Transactional
    public boolean deleteEmployee(String name) {
        checkIfExists(name);
        employeeRepository.deleteByName(name);
        return true;
    }

    @Transactional
    public boolean assignPosition(String employeeName, Integer positionId) {
        Employee employee = checkIfExists(employeeName);
        Position position = positionService.findForAssigment(positionId);
        employee.setPosition(position);
        return true;
    }

    private Employee checkIfExists(String name) {
        return employeeRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Employee", "id", name)
        );
    }

    private EmployeeDto convertToEmployeeDto(Employee employee) {
        return modelMapper.map(employee, EmployeeDto.class);
    }

    private Position convertToPosition(PositionDto positionDto) {
        return modelMapper.map(positionDto, Position.class);
    }
}
