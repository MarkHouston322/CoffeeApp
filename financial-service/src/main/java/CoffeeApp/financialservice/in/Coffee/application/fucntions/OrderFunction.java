package CoffeeApp.financialservice.in.Coffee.application.fucntions;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.PaymentMessage;
import CoffeeApp.financialservice.in.Coffee.application.services.TransactionsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class OrderFunction {

    @Bean
    public Consumer<PaymentMessage> sendPayment(TransactionsService transactionsService){
        return transactionsService :: paymentTransaction;
    }
}
