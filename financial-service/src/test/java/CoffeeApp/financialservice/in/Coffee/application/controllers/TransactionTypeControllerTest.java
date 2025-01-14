package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.TransactionTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class TransactionTypeControllerTest {

    @Mock
    private TransactionTypeService transactionTypeService;

    @InjectMocks
    private TransactionTypeController transactionTypeController;

    @Test
    void shouldReturnAllTransactionTypesDtoAndStatus200() throws Exception {
        // given
        when(transactionTypeService.findAll()).thenReturn(new TransactionTypeResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactionTypes");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"transactionTypes\":[]}"));
    }

    @Test
    void shouldReturnTransactionTypesDtoByNameStartingWithAndStatus200() throws Exception {
        // given
        String name = "Cash";
        when(transactionTypeService.findTransactionTypeByName(Mockito.anyString())).thenReturn(new TransactionTypeResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/transactionTypes/{name}",name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"transactionTypes\":[]}"));
    }

    @Test
    void shouldAddTransactionTypeAndReturnStatus200() throws Exception {
        // given
        doNothing().when(transactionTypeService).addTransactionType(Mockito.any());
        TransactionTypeDto typeDto = new TransactionTypeDto("Cash");
        String requestBody = new ObjectMapper().writeValueAsString(typeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/transactionTypes/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Transaction type added successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(transactionTypeController)
                .build()
                .perform(requestBuilder);
    }

}