package CoffeeApp.financialservice.in.Coffee.application.mappers;

import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.models.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionMapper {

    public static TransactionDto mapToTransactionDto(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setDate(dateFormatter(transaction.getDate()));
        transactionDto.setType(transaction.getType());
        transactionDto.setSum(transaction.getSum());
        transactionDto.setSession(transaction.getSession());
        return transactionDto;
    }

    private static String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
