package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.dto.drinkDto.AddDrinkDto;
import CoffeeApp.storageservice.dto.drinkDto.DrinkResponse;
import CoffeeApp.storageservice.dto.drinkDto.ShowDrinkDto;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.services.DrinkService;
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
class DrinkControllerTest {

    @Mock
    private DrinkService drinkService;

    @InjectMocks
    private DrinkController drinkController;

    @Test
    void shouldReturnAllDrinksDtoAndStatus200() throws Exception {
       // when
       when(drinkService.findAll()).thenReturn(new DrinkResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/drinks");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"drinks\":[]}"));
        verify(drinkService).findAll();
    }

    @Test
    void shouldReturnDrinkDtoByIdAndStatus200() throws Exception {
        // given
        int id = 1;
        ShowDrinkDto showDrinkDto = new ShowDrinkDto();
        when(drinkService.findById(Mockito.anyInt())).thenReturn(showDrinkDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/drinks/{id}", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"name\":null,\"price\":null,\"costPrice\":null,\"category\":null,\"soldQuantity\":null,\"writeOffQuantity\":null,\"surchargeRatio\":null}"));
        verify(drinkService).findById(id);
    }

    @Test
    void shouldReturnIngredientsDtoByDrinkIdAndStatus200() throws Exception {
        // given
        int id = 1;
        when(drinkService.getIngredientsByDrinkId(Mockito.anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/drinks/{id}/ingredients", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("[]"));
        verify(drinkService).getIngredientsByDrinkId(id);
    }

    @Test
    void shouldReturnDrinksDtoByNameStartingWithAndStatus200() throws Exception {
        // when
        String name = "Wa";
        when(drinkService.findDrinksByName(name)).thenReturn(new DrinkResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/drinks/get/{name}", name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"drinks\":[]}"));
        verify(drinkService).findDrinksByName(name);
    }

    @Test
    void shouldAddDrinkAndReturnStatus201() throws Exception {
        // given
        doNothing().when(drinkService).addDrink(Mockito.any(),Mockito.anyMap());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/drinks/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Drink added successfully\"}"));

    }

    @Test
    void shouldDeleteDrinkAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(drinkService).deleteDrinkById(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/drinks/{id}/delete", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(drinkController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody() throws JsonProcessingException {
        AddDrinkDto bonusDto = new AddDrinkDto("Latte",new Category(),2f);
        return new ObjectMapper().writeValueAsString(bonusDto);
    }

}