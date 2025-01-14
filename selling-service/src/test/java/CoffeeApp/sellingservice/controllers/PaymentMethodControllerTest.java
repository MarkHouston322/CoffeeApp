package CoffeeApp.sellingservice.controllers;

import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodResponse;
import CoffeeApp.sellingservice.services.PaymentMethodService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@ExtendWith(MockitoExtension.class)
class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    @Test
    void shouldReturnAllPaymentMethodsDtoAndStatus200() throws Exception {
        // given
        when(paymentMethodService.findAll()).thenReturn(new PaymentMethodResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"paymentMethods\":[]}"));
        verify(paymentMethodService).findAll();
    }

    @Test
    void shouldReturnPaymentMethodDtoByIdAndStatus200() throws Exception{
        // given
        int id =  1;
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto("Cash");
        when(paymentMethodService.findById(id)).thenReturn(paymentMethodDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment/{id}",id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"name\":\"Cash\"}"));
        verify(paymentMethodService).findById(id);
    }

    @Test
    void shouldReturnPaymentMethodsDtoByNameStartingWithAndStatus200() throws Exception {
        // given
        String name = "Ca";
        when(paymentMethodService.findByName(name)).thenReturn(new PaymentMethodResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/payment/name/{name}", name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"paymentMethods\":[]}"));
        verify(paymentMethodService).findByName(name);
    }

    @Test
    void shouldAddPaymentMethodAndReturnStatus201() throws  Exception {
        // given
        doNothing().when(paymentMethodService).addPaymentMethod(Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/payment/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Payment method added successfully\"}"));
        verify(paymentMethodService).addPaymentMethod(Mockito.any());
    }

    @Test
    void shouldUpdatePaymentMethodByIdAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(paymentMethodService).updatePaymentMethod(Mockito.anyInt(),Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/payment/{id}/update", id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
        verify(paymentMethodService).updatePaymentMethod(Mockito.anyInt(),Mockito.any());
    }

    @Test
    void shouldDeletePaymentMethodByIdAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(paymentMethodService).deletePaymentMethod(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/payment/{id}/delete", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
        verify(paymentMethodService).deletePaymentMethod(Mockito.anyInt());
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(paymentMethodController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody() throws JsonProcessingException {
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto("Cash");
        return  new ObjectMapper().writeValueAsString(paymentMethodDto);
    }

}