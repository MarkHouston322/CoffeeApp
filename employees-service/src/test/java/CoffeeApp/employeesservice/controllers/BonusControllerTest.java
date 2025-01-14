package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.dto.bonusDto.BonusDto;
import CoffeeApp.employeesservice.dto.bonusDto.BonusResponse;
import CoffeeApp.employeesservice.services.BonusService;
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
class BonusControllerTest {

    @Mock
    private BonusService bonusService;

    @InjectMocks
    private BonusController bonusController;

    @Test
    void shouldReturnAllBonusesAndStatus200() throws Exception {
        // given
        when(bonusService.findAll()).thenReturn(new BonusResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bonus");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"bonuses\":[]}"));
    }

    @Test
    void shouldReturnBonusDtoByIdAndStatus200() throws Exception {
        // given
        int bonusId = 1;
        BonusDto bonusDto = new BonusDto(1000,500);
        when(bonusService.findById(Mockito.anyInt())).thenReturn(bonusDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bonus/{id}", bonusId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"edge\":1000,\"value\":500}"));
        verify(bonusService).findById(bonusId);
    }

    @Test
    void shouldReturnBonusDtoByEdgeAndStatus200() throws Exception {
        // given
        int edge = 1000;
        BonusDto bonusDto = new BonusDto(edge,500);
        when(bonusService.findByEdge(Mockito.anyInt())).thenReturn(bonusDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bonus/get/{edge}", edge);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"edge\":1000,\"value\":500}"));
        verify(bonusService).findByEdge(edge);
    }

    @Test
    void shouldAddBonusAndReturnStatus201() throws Exception {
        // given
        doNothing().when(bonusService).addBonus(Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/bonus/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Bonus added successfully\"}"));

    }

    @Test
    void shouldUpdateBonusAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(bonusService).updateBonus(Mockito.anyInt(),Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/bonus/{id}/update", id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    @Test
    void shouldDeleteBonusAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(bonusService).deleteBonus(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/bonus/{id}/delete", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(bonusController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody() throws JsonProcessingException {
        BonusDto bonusDto = new BonusDto(1000, 500);
        return new ObjectMapper().writeValueAsString(bonusDto);
    }
}