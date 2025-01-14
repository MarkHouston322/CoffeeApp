package CoffeeApp.customerservice.controllers;

import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelDto;
import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelResponse;
import CoffeeApp.customerservice.services.LoyaltyLevelService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class LoyaltyLevelControllerTest {

    @Mock
    private LoyaltyLevelService levelService;

    @InjectMocks
    private LoyaltyLevelController levelController;

    @Test
    void shouldReturnAllLoyaltyLevelAndStatus200() throws Exception {
        // given
        when(levelService.findAll()).thenReturn(new LoyaltyLevelResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loyalty");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("<LoyaltyLevelResponse><loyaltyLevels/></LoyaltyLevelResponse>"));

    }

    @Test
    void shouldReturnLoyaltyLevelDtoByIdAndStatus200() throws Exception {
        // given
        int levelId = 1;
        LoyaltyLevelDto levelDto = new LoyaltyLevelDto("PRO", 1000, 0.1f);
        when(levelService.findById(Mockito.anyInt())).thenReturn(levelDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loyalty/{id}", levelId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string(
                                "<LoyaltyLevelDto><name>PRO</name><edge>1000</edge>" +
                                        "<discountPercentage>0.1</discountPercentage></LoyaltyLevelDto>"));
        verify(levelService).findById(levelId);
    }

    @Test
    void shouldReturnLoyaltyLevelDtoByNameAndStatus200() throws Exception {
        // given
        String name = "Name";
        when(levelService.findByName(Mockito.anyString())).thenReturn(new LoyaltyLevelResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loyalty/get/{name}", name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("<LoyaltyLevelResponse><loyaltyLevels/></LoyaltyLevelResponse>"));
    }

    @Test
    void shouldAddLoyaltyLevelAndReturnStatus201() throws Exception {
        // given
        doNothing().when(levelService).addLoyaltyLevel(Mockito.<LoyaltyLevelDto>any());
        LoyaltyLevelDto levelDto = new LoyaltyLevelDto("PRO", 1000, 0.1f);
        String requestBody = new ObjectMapper().writeValueAsString(levelDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/loyalty/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("<ResponseDto><statusCode>201</statusCode><statusMsg>Loyalty level added successfully</statusMsg><"
                                + "/ResponseDto>"));
    }

    @Test
    void shouldUpdateLoyaltyLevelAndReturnStatus200() throws Exception {
        // given
        doNothing().when(levelService).updateLoyaltyService(Mockito.anyInt(),Mockito.any());
        MockHttpServletRequestBuilder requestBuilder = setRequestBuilderForUpdate();
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("<ResponseDto><statusCode>200</statusCode><statusMsg>Request processed successfully</statusMsg><"
                                + "/ResponseDto>"));
    }

    @Test
    void shouldDeleteLoyaltyLevelAndReturnStatus200() throws Exception {
        // given
        doNothing().when(levelService).deleteLoyaltyLevel(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/loyalty/{id}/delete", 1);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("<ResponseDto><statusCode>200</statusCode><statusMsg>Request processed successfully</statusMsg><"
                                + "/ResponseDto>"));
    }

    private MockHttpServletRequestBuilder setRequestBuilderForUpdate() throws JsonProcessingException {
        LoyaltyLevelDto levelDto = new LoyaltyLevelDto("PRO", 1000, 0.1f);
        String requestBody = new ObjectMapper().writeValueAsString(levelDto);
        return MockMvcRequestBuilders
                .patch("/loyalty/{id}/update", 1)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(levelController)
                .build()
                .perform(requestBuilder);
    }
}
