package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.AddTransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.PaymentMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionsResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.mappers.TransactionMapper;
import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.models.Transactions;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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

    public TransactionsResponse findAll(){
        return new TransactionsResponse(transactionRepository.findAll().stream().map(this::convertToTransactionDto)
                .collect(Collectors.toList()));
    }

    public TransactionDto findById(Integer id){
        Transactions transaction = checkIfExists(id);
        return TransactionMapper.mapToTransactionDto(transaction);
    }

    public TransactionsResponse findByType(String name){
        return new TransactionsResponse(transactionRepository.findByTypeName(name).stream().map(this::convertToTransactionDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void paymentTransaction(PaymentMessage paymentMessage){
        Session session = sessionService.findCurrentSession();
        if (paymentMessage.getTransactionType().equals("credit card")){
            Transactions cardPayment = new Transactions(transactionTypeService.findByName("Card payment"), paymentMessage.getSum());
            cardPayment.setDate(LocalDateTime.now());
            cardPayment.setSession(session);
            transactionRepository.save(cardPayment);
            sessionService.cardPayment(session, cardPayment.getSum());
            totalFinanceService.insertCard(cardPayment.getSum());
        } else if (paymentMessage.getTransactionType().equals("cash")) {
            Transactions cashPayment = new Transactions(transactionTypeService.findByName("Cash payment"), paymentMessage.getSum());
            cashPayment.setDate(LocalDateTime.now());
            cashPayment.setSession(session);
            transactionRepository.save(cashPayment);
            sessionService.cashPayment(session, cashPayment.getSum());
            totalFinanceService.insertCash(cashPayment.getSum());
        }

    }

    @Transactional
    public void createCashInflow(AddTransactionDto addTransactionDto){
        Transactions cashInflow = convertToTransaction(addTransactionDto);
        Session session = sessionService.findCurrentSession();
        cashInflow.setSession(session);
        cashInflow.setDate(LocalDateTime.now());
        cashInflow.setType(transactionTypeService.findByName("Cash deposit"));
        transactionRepository.save(cashInflow);
        sessionService.cashInflow(session, cashInflow.getSum());
    }

    @Transactional
    public void createCashWithdrawal(AddTransactionDto addTransactionDto){
        Transactions cashWithdrawal = convertToTransaction(addTransactionDto);
        Session session = sessionService.findCurrentSession();
        cashWithdrawal.setSession(session);
        cashWithdrawal.setDate(LocalDateTime.now());
        cashWithdrawal.setType(transactionTypeService.findByName("Cash withdrawal"));
        transactionRepository.save(cashWithdrawal);
        sessionService.cashWithdrawal(session, cashWithdrawal.getSum());
    }

    private Transactions checkIfExists(int id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Transaction ", "id", Integer.toString(id))
        );
    }

    private TransactionDto convertToTransactionDto(Transactions transaction){
        return modelMapper.map(transaction, TransactionDto.class);
    }

    private Transactions convertToTransaction(AddTransactionDto addTransactionDto){
        return modelMapper.map(addTransactionDto, Transactions.class);
    }
}
