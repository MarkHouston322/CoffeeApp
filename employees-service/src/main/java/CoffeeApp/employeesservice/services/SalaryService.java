package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.messages.OrderMessage;
import CoffeeApp.employeesservice.dto.messages.SessionMessage;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryDto;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryResponse;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Bonus;
import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.models.Salary;
import CoffeeApp.employeesservice.repositories.SalaryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SalaryService {

    private final SalaryRepository salaryRepository;
    private final EmployeeService employeeService;
    private final BonusService bonusService;
    private final ModelMapper modelMapper;


    public SalaryDto findById(Integer id){
        Salary salary = checkIfExists(id);
        return convertToSalaryDto(salary);
    }

    public SalaryResponse findAll(){
        return new SalaryResponse(salaryRepository.findAll().stream().map(this::convertToSalaryDto)
                .collect(Collectors.toList()));
    }

    public SalaryResponse findByEmployeeName(String name){
        Employee employee = employeeService.findByName(name);
        return new SalaryResponse(salaryRepository.findByEmployee(employee).stream().map(this::convertToSalaryDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void openSalarySession (SessionMessage sessionMessage){
        Salary salary = new Salary();
        Employee employee;
        try {
             employee = employeeService.findByName(sessionMessage.getEmployeeUsername());
        } catch (ResourceNotFoundException e){
            employee = employeeService.addEmployee(sessionMessage.getEmployeeUsername());
        }
        Position position = employee.getPosition();
        salary.setEmployee(employee);
        salary.setDate(LocalDateTime.now());
        salary.setRevenue(0);
        salary.setSalary(position.getSalary());
        salary.setRoyalty(position.getRoyalty());
        salary.setBonus(0);
        salary.setTotal(salary.getSalary());
        salary.setIsClosed(false);
        salaryRepository.save(salary);
    }


    @Transactional
    public void updateSalary(OrderMessage orderMessage){
        Salary salary = findCurrentSalarySession();
        if (!Objects.equals(salary.getEmployee().getName(), orderMessage.getEmployeeName())){
            closeSalarySession();
            openSalarySession(new SessionMessage(orderMessage.getEmployeeName(),false));
        } else {
            salary.setRevenue(salary.getRevenue() + orderMessage.getSum());
            salary.setBonus(bonusService.findByRequiredEdge(salary.getRevenue()).getValue());
            salary.setTotal(salary.getSalary() + (salary.getRoyalty() * salary.getRevenue()) + salary.getBonus());
        }
    }

    @Transactional
    public boolean deleteSalary(Integer id){
        checkIfExists(id);
        salaryRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void closeSalarySession(){
        Salary currentSalarySession = findCurrentSalarySession();
        currentSalarySession.setIsClosed(true);
    }

    private Salary checkIfExists(int id) {
        return salaryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Salary", "id", Integer.toString(id))
        );
    }

    public Salary findCurrentSalarySession(){
        return salaryRepository.findLastOpenSalarySession().orElseThrow(
                () -> new ResourceNotFoundException("Salary session", "is closed", "false")
        );
    }

    private SalaryDto convertToSalaryDto(Salary salary){
        return modelMapper.map(salary, SalaryDto.class);
    }
}
