package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.dto.bonusDto.BonusDto;
import CoffeeApp.employeesservice.dto.bonusDto.BonusResponse;
import CoffeeApp.employeesservice.services.BonusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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
                .andExpect(content().string("<BonusResponse><bonuses/></BonusResponse>"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(bonusController)
                .build()
                .perform(requestBuilder);
    }

    private MockHttpServletRequestBuilder setRequestBuilderForUpdate() throws JsonProcessingException {
        BonusDto bonusDto = new BonusDto(1000, 500);
        String requestBody = new ObjectMapper().writeValueAsString(bonusDto);
        return MockMvcRequestBuilders
                .patch("/bonus/{id}/update", 1)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
    }

}