package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.AddTransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactionDto.TransactionsResponse;
import CoffeeApp.financialservice.in.Coffee.application.models.Session;
import CoffeeApp.financialservice.in.Coffee.application.models.TransactionType;
import CoffeeApp.financialservice.in.Coffee.application.services.TransactionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionsService transactionsService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void shouldReturnAllTransactionsDtoAndStatus200() throws Exception {
        // given
        when(transactionsService.findAll()).thenReturn(new TransactionsResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"transactions\":[]}"));
    }

    @Test
    void shouldReturnTransactionDtoByIdAndStatus200() throws Exception {
        // given
        int id  = 1;
        TransactionDto transactionDto = new TransactionDto(null,new TransactionType(),1000,new Session());
        when(transactionsService.findById(Mockito.anyInt())).thenReturn(transactionDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions/{id}",id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"date\":null,\"type\":{\"name\":null},\"sum\":1000}"));
    }

    @Test
    void shouldReturnTransactionsDtoByTypeNameAndStatus200() throws Exception {
        // given
        String typeName = "Cash";
        when(transactionsService.findByType(Mockito.anyString())).thenReturn(new TransactionsResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactions/transactionType/{type}", typeName);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"transactions\":[]}"));
    }

    @Test
    void shouldCreateCashInflowAndReturnStatus201() throws Exception {
        // given
        doNothing().when(transactionsService).createCashInflow(Mockito.any(AddTransactionDto.class));
        String requestBody = new ObjectMapper().writeValueAsString(new AddTransactionDto(1000));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/transactions/cashInflow")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Transaction added successfully\"}"));
    }

    @Test
    void shouldCreateCashWithdrawalAndReturnStatus201() throws Exception {
        // given
        doNothing().when(transactionsService).createCashInflow(Mockito.any(AddTransactionDto.class));
        String requestBody = new ObjectMapper().writeValueAsString(new AddTransactionDto(1000));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/transactions/cashWithdrawal")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Transaction added successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(transactionController)
                .build()
                .perform(requestBuilder);
    }

}