package CoffeeApp.sellingservice.controllers;

import CoffeeApp.sellingservice.dto.orderDto.AddOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderResponse;
import CoffeeApp.sellingservice.models.PaymentMethod;
import CoffeeApp.sellingservice.services.OrderService;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void shouldReturnAllOrdersDtoAndStatus200() throws Exception {
        // given
        when(orderService.findAll()).thenReturn(new OrderResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"orders\":[]}"));
        verify(orderService).findAll();
    }

    @Test
    void shouldReturnOrdersDtoByIdAndStatus200() throws Exception{
        int id = 1;
        OrderDto orderDto = new OrderDto();
        when(orderService.findById(id)).thenReturn(orderDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/{id}",id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":null,\"orderDate\":null,\"total\":null," +
                        "\"discount\":null,\"totalWithDsc\":null,\"paymentMethod\":null,\"customerId\":null," +
                        "\"sessionId\":null,\"employeeName\":null,\"customerPurchaseConfirmation\":null}"));
        verify(orderService).findById(id);
    }

    @Test
    void shouldReturnGoodsInOrderByIdAndStatus200() throws Exception {
        // given
        int id = 1;
        when(orderService.getGoodsInOrder(1)).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/orders/{id}/drinks", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
        verify(orderService).getGoodsInOrder(id);
    }

    @Test
    void shouldAddOrderAndReturnStatus201() throws Exception {
        // given
        String customerId = "12345";
        int method_id = 1;
        PaymentMethod paymentMethod = new PaymentMethod("Cash");
        paymentMethod.setId(method_id);
        doNothing().when(orderService).addOrder(Mockito.any(AddOrderDto.class), Mockito.anyMap(), Mockito.eq(customerId));
        String requestBody = createRequestBody(paymentMethod);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/orders/add/{customer_id}", customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .param("drinks[Coffee]", "2")
                .param("drinks[Tea]", "1");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Order added successfully\"}"));
        verify(orderService).addOrder(Mockito.any(AddOrderDto.class), Mockito.anyMap(), Mockito.eq(customerId));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody(PaymentMethod paymentMethod) throws JsonProcessingException {
        AddOrderDto addOrderDto = new AddOrderDto(paymentMethod);
        return new ObjectMapper().writeValueAsString(addOrderDto);
    }
}
