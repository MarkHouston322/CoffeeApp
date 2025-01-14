package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.dto.totalFinanceDto.TotalFinanceResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.TotalFinanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class TotalFinanceControllerTest {

    @Mock
    private TotalFinanceService totalFinanceService;

    @InjectMocks
    private TotalFinanceController totalFinanceController;

    @Test
    void shouldReturnTotalFinanceDtoAndStatus200() throws Exception {
        // given
        when(totalFinanceService.get()).thenReturn(new TotalFinanceResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/totalFinance");
        // when & then
        MockMvcBuilders.standaloneSetup(totalFinanceController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"totalFinance\":[]}"));
    }
}