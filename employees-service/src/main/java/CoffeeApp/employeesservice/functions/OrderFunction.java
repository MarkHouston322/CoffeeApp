package CoffeeApp.employeesservice.functions;

import CoffeeApp.employeesservice.dto.messages.OrderMessage;
import CoffeeApp.employeesservice.services.SalaryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderFunction {

    @Bean
    public Consumer<OrderMessage> getRevenue(SalaryService salaryService){
        return salaryService :: updateSalary;
    }
}
