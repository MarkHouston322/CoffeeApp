package CoffeeApp.financialservice.in.Coffee.application.controllers;

import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.sessionDto.SessionResponse;
import CoffeeApp.financialservice.in.Coffee.application.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;


    @Test
    void shouldReturnAllSessionsDtoAndStatus200() throws Exception {
        // given
        when(sessionService.findAll()).thenReturn(new SessionResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sessions");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"dailyFinances\":[]}"));
        verify(sessionService).findAll();

    }

    @Test
    void shouldReturnSessionByIdAndStatus200() throws Exception {
        // given
        int sessionId = 1;
        SessionDto sessionDto = new SessionDto();
        when(sessionService.findById(Mockito.anyInt())).thenReturn(sessionDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sessions/{id}", sessionId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"openingDate\":null,\"employeeUsername\":null,\"initialCash\":null," +
                                "\"cashInflow\":null,\"cashExpense\":null,\"cardPayment\":null,\"cashPayment\":null," +
                                "\"ordersQuantity\":null,\"totalMoney\":null,\"finiteCash\":null,\"closingDate\":null," +
                                "\"sessionIsClosed\":null}"));
        verify(sessionService).findById(sessionId);
    }

    @Test
    void shouldOpenSessionAndReturnStatus201() throws Exception {
        // given
        String username = "Test";
        doNothing().when(sessionService).openSession(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/sessions/open")
                .header("Preferred-Username", username);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Session opened successfully\"}"));
        verify(sessionService).openSession(Mockito.anyString());

    }

    @Test
    void shouldCloseSessionAndReturnStatus200() throws Exception {
        // when
        doNothing().when(sessionService).closeSession();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/sessions/close");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Session closed successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(sessionController)
                .build()
                .perform(requestBuilder);
    }

}