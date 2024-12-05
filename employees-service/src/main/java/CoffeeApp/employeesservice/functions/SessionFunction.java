package CoffeeApp.employeesservice.functions;

import CoffeeApp.employeesservice.dto.messages.SessionMessage;
import CoffeeApp.employeesservice.services.SalaryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class SessionFunction {

    @Bean
    public Consumer<SessionMessage> getEmployee(SalaryService salaryService){
        return sessionMessage -> {
            if (!sessionMessage.getIsClosed()){
                salaryService.openSalarySession(sessionMessage);
            } else {
                salaryService.closeSalarySession();
            }
        };
    }
}
