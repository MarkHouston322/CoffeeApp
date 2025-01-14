package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.PaymentMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedTransactionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.AddTransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionsResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.models.Transaction;
import CoffeeApp.financialservice.in.Coffee.application.models.TransactionType;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SessionService sessionService;

    @Mock
    private TransactionTypeService transactionTypeService;

    @Mock
    private TotalFinanceService totalFinanceService;

    @Mock
    private KafkaTemplate<String, ProceedTransactionMessage> salaryKafkaTemplate;

    @InjectMocks
    private TransactionsService transactionsService;

    private final TransactionType cashType = new TransactionType("Cash");
    private final TransactionType cardType = new TransactionType("Card");

    @Test
    void shouldReturnAllTransactionsDto(){
        // given
        Transaction transaction1 = new Transaction(cashType,1000);
        Transaction transaction2 = new Transaction(cardType,2000);
        List<Transaction> transactions = Arrays.asList(transaction1,transaction2);
        TransactionDto transactionDto1 = new TransactionDto(null,cashType,1000,null);
        TransactionDto transactionDto2 = new TransactionDto(null,cardType,2000,null);
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(modelMapper.map(transaction1,TransactionDto.class)).thenReturn(transactionDto1);
        when(modelMapper.map(transaction2,TransactionDto.class)).thenReturn(transactionDto2);
        // when
        TransactionsResponse response = transactionsService.findAll();
        // then
        assertThat(response.getTransactions()).containsExactly(transactionDto1,transactionDto2);
        verify(transactionRepository).findAll();
    }

    @Test
    void shouldNotReturnAllTransactionsDto(){
        // given
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        TransactionsResponse response = transactionsService.findAll();
        // then
        assertThat(response.getTransactions()).isEmpty();
        verify(transactionRepository).findAll();
    }

    @Test
    void shouldReturnTransactionsDtoByTypeName(){
        // given
        String typeName = "Cash";
        Transaction transaction1 = new Transaction(cashType,1000);
        Transaction transaction2 = new Transaction(cashType,2000);
        List<Transaction> transactions = Arrays.asList(transaction1,transaction2);
        TransactionDto transactionDto1 = new TransactionDto(null,cashType,1000,null);
        TransactionDto transactionDto2 = new TransactionDto(null,cashType,2000,null);
        when(transactionRepository.findByTypeName(typeName)).thenReturn(transactions);
        when(modelMapper.map(transaction1,TransactionDto.class)).thenReturn(transactionDto1);
        when(modelMapper.map(transaction2,TransactionDto.class)).thenReturn(transactionDto2);
        // when
        TransactionsResponse response = transactionsService.findByType(typeName);
        // then
        assertThat(response.getTransactions()).containsExactly(transactionDto1,transactionDto2);
        verify(transactionRepository).findByTypeName(typeName);
    }

    @Test
    void shouldNotReturnTransactionsDtoByTypeName(){
        // given
        String typeName = "Cash";
        when(transactionRepository.findByTypeName(typeName)).thenReturn(Collections.emptyList());
        // when
        TransactionsResponse response = transactionsService.findByType(typeName);
        // then
        assertThat(response.getTransactions()).isEmpty();
        verify(transactionRepository).findByTypeName(typeName);
    }

    @Test
    void shouldReturnTransactionDtoById(){
        // given
        int id = 1;
        Transaction transaction = new Transaction(LocalDateTime.now(),cashType,1000,null);
        transaction.setId(id);
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        // when
        TransactionDto result = transactionsService.findById(id);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo(transaction.getType());
        assertThat(result.getSum()).isEqualTo(transaction.getSum());
        verify(transactionRepository).findById(id);
    }

    @Test
    void shouldNotReturnTransactionDtoById(){
        // given
        int id = 1;
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> transactionsService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction", "id", Integer.toString(id));
        verify(transactionRepository).findById(id);
    }

    @Test
    void shouldHandleCreditCardPaymentTransactionSuccessfully() {
        // given
        PaymentMessage paymentMessage = new PaymentMessage("credit card", 2000);

        Session mockSession = new Session();
        mockSession.setId(1);

        TransactionType mockTransactionType = new TransactionType();
        mockTransactionType.setName("Card payment");

        when(sessionService.findCurrentSession()).thenReturn(mockSession);
        when(transactionTypeService.findByName("Card payment")).thenReturn(mockTransactionType);

        doNothing().when(sessionService).cardPayment(mockSession, paymentMessage.getSum());
        doNothing().when(totalFinanceService).insertCard(paymentMessage.getSum());

        // when
        transactionsService.paymentTransaction(paymentMessage);
        // then
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.getType()).isEqualTo(mockTransactionType);
        assertThat(savedTransaction.getSum()).isEqualTo(paymentMessage.getSum());
        assertThat(savedTransaction.getSession()).isEqualTo(mockSession);
        assertThat(savedTransaction.getDate()).isNotNull();

        verify(sessionService).cardPayment(mockSession, paymentMessage.getSum());
        verify(totalFinanceService).insertCard(paymentMessage.getSum());
        testKafka("Card payment");
    }

    @Test
    void shouldHandleCashPaymentTransactionSuccessfully() {
        // given
        PaymentMessage paymentMessage = new PaymentMessage("cash", 1500);

        Session mockSession = new Session();
        mockSession.setId(1);

        TransactionType mockTransactionType = new TransactionType();
        mockTransactionType.setName("Cash payment");

        when(sessionService.findCurrentSession()).thenReturn(mockSession);
        when(transactionTypeService.findByName("Cash payment")).thenReturn(mockTransactionType);

        doNothing().when(sessionService).cashPayment(mockSession, paymentMessage.getSum());
        doNothing().when(totalFinanceService).insertCash(paymentMessage.getSum());
        // when
        transactionsService.paymentTransaction(paymentMessage);
        // then
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.getType()).isEqualTo(mockTransactionType);
        assertThat(savedTransaction.getSum()).isEqualTo(paymentMessage.getSum());
        assertThat(savedTransaction.getSession()).isEqualTo(mockSession);
        assertThat(savedTransaction.getDate()).isNotNull();

        verify(sessionService).cashPayment(mockSession, paymentMessage.getSum());
        verify(totalFinanceService).insertCash(paymentMessage.getSum());
        testKafka("Cash payment");
    }

    @Test
    void shouldThrowExceptionForUnknownTransactionType() {
        // given
        PaymentMessage paymentMessage = new PaymentMessage("unknown type", 1000);
        // when & then
        assertThatThrownBy(() -> transactionsService.paymentTransaction(paymentMessage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown transaction type: unknown type");
        verifyNoInteractions(sessionService, transactionTypeService, transactionRepository, totalFinanceService, salaryKafkaTemplate);
    }

    @Test
    void shouldCreateCashInflow(){
        // given
        Session session = new Session();
        TransactionType transactionType = new TransactionType("Cash deposit");
        Transaction cashInflow = new Transaction(transactionType,1000);
        AddTransactionDto addTransactionDto = new AddTransactionDto(1000);
        when(sessionService.findCurrentSession()).thenReturn(session);
        when(transactionTypeService.findByName(transactionType.getName())).thenReturn(transactionType);
        when(modelMapper.map(addTransactionDto, Transaction.class)).thenReturn(cashInflow);
        // when
        transactionsService.createCashInflow(addTransactionDto);
        // then
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction result  = transactionCaptor.getValue();
        assertThat(result.getType()).isEqualTo(transactionType);
        assertThat(result.getSession()).isEqualTo(session);
        assertThat(result.getSum()).isEqualTo(cashInflow.getSum());
    }

    @Test
    void shouldCreateCashWithdrawal(){
        // given
        Session session = new Session();
        TransactionType transactionType = new TransactionType("Cash withdrawal");
        Transaction cashWithdrawal = new Transaction(transactionType,1000);
        AddTransactionDto addTransactionDto = new AddTransactionDto(1000);
        when(sessionService.findCurrentSession()).thenReturn(session);
        when(transactionTypeService.findByName(transactionType.getName())).thenReturn(transactionType);
        when(modelMapper.map(addTransactionDto, Transaction.class)).thenReturn(cashWithdrawal);
        // when
        transactionsService.createCashWithdrawal(addTransactionDto);
        // then
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction result  = transactionCaptor.getValue();
        assertThat(result.getType()).isEqualTo(transactionType);
        assertThat(result.getSession()).isEqualTo(session);
        assertThat(result.getSum()).isEqualTo(cashWithdrawal.getSum());
    }

    private void testKafka(String transactionType){
        ArgumentCaptor<ProceedTransactionMessage> kafkaMessageCaptor = ArgumentCaptor.forClass(ProceedTransactionMessage.class);
        verify(salaryKafkaTemplate).send(eq("transactions"), kafkaMessageCaptor.capture());
        ProceedTransactionMessage sentMessage = kafkaMessageCaptor.getValue();
        assertThat(sentMessage.getDate()).isNotNull();
        assertThat(sentMessage.getTransactionType()).isEqualTo(transactionType);
        assertThat(sentMessage.getSum()).isEqualTo(1500);
    }


}