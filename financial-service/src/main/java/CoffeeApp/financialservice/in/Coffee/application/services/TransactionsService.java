package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedTransactionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.AddTransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.PaymentMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionsResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.mappers.TransactionMapper;
import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.models.Transaction;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final TransactionTypeService transactionTypeService;
    private final SessionService sessionService;
    private final TotalFinanceService totalFinanceService;
    private final KafkaTemplate<String, ProceedTransactionMessage> transactionKafkaTemplate;

    public TransactionsResponse findAll(){
        return new TransactionsResponse(transactionRepository.findAll().stream().map(this::convertToTransactionDto)
                .collect(Collectors.toList()));
    }

    public TransactionDto findById(Integer id){
        Transaction transaction = checkIfExists(id);
        return convertToTransactionDto(transaction);
    }

    public TransactionsResponse findByType(String name){
        return new TransactionsResponse(transactionRepository.findByTypeName(name).stream().map(this::convertToTransactionDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void paymentTransaction(PaymentMessage paymentMessage) {
        String transactionTypeString = paymentMessage.getTransactionType();
        String transactionTypeName = switch (transactionTypeString) {
            case "credit card" -> "Card payment";
            case "cash"        -> "Cash payment";
            default -> throw new IllegalArgumentException("Unknown transaction type: " + transactionTypeString);
        };
        Session session = sessionService.findCurrentSession();
        Transaction transaction = new Transaction(
                transactionTypeService.findByName(transactionTypeName),
                paymentMessage.getSum()
        );
        transaction.setDate(LocalDateTime.now());
        transaction.setSession(session);
        transactionRepository.save(transaction);

        int sum = transaction.getSum();
        if ("credit card".equals(transactionTypeString)) {
            sessionService.cardPayment(session, sum);
            totalFinanceService.insertCard(sum);
        } else if ("cash".equals(transactionTypeString)) {
            sessionService.cashPayment(session, sum);
            totalFinanceService.insertCash(sum);
        }
        sendTransactionMessage(transaction, transactionKafkaTemplate);
    }


    @Transactional
    public void createCashInflow(AddTransactionDto addTransactionDto){
        Transaction cashInflow = convertToTransaction(addTransactionDto);
        Session session = sessionService.findCurrentSession();
        cashInflow.setSession(session);
        cashInflow.setDate(LocalDateTime.now());
        cashInflow.setType(transactionTypeService.findByName("Cash deposit"));
        transactionRepository.save(cashInflow);
        sessionService.cashInflow(session, cashInflow.getSum());
    }

    @Transactional
    public void createCashWithdrawal(AddTransactionDto addTransactionDto){
        Transaction cashWithdrawal = convertToTransaction(addTransactionDto);
        Session session = sessionService.findCurrentSession();
        cashWithdrawal.setSession(session);
        cashWithdrawal.setDate(LocalDateTime.now());
        cashWithdrawal.setType(transactionTypeService.findByName("Cash withdrawal"));
        transactionRepository.save(cashWithdrawal);
        sessionService.cashWithdrawal(session, cashWithdrawal.getSum());
    }

    private void sendTransactionMessage(Transaction transaction,KafkaTemplate<String, ProceedTransactionMessage> transactionKafkaTemplate){
        ProceedTransactionMessage transactionMessage = new ProceedTransactionMessage(LocalDateTime.now(),transaction.getType().getName(), transaction.getSum());
        transactionKafkaTemplate.send("transactions",transactionMessage);
    }

    private Transaction checkIfExists(int id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Transaction ", "id", Integer.toString(id))
        );
    }

    private TransactionDto convertToTransactionDto(Transaction transaction){
        return TransactionMapper.mapToTransactionDto(transaction);
    }

    private Transaction convertToTransaction(AddTransactionDto addTransactionDto){
        return modelMapper.map(addTransactionDto, Transaction.class);
    }
}
