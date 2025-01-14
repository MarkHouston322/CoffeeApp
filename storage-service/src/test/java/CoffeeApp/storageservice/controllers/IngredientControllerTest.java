package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.dto.ingredientDto.IngredientDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientResponse;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
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
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @Test
    void shouldReturnAllIngredientsDtoAndStatus200() throws Exception{
        // given
        when(ingredientService.findAll()).thenReturn(new IngredientResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ingredients");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"ingredients\":[]}"));
        verify(ingredientService).findAll();
    }

    @Test
    void shouldReturnIngredientDtoByIdAndStatus200() throws Exception {
        // given
        int id = 1;
        IngredientDto ingredientDto = new IngredientDto("Milk",90f,3000f);
        when(ingredientService.findById(id)).thenReturn(ingredientDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ingredients/{id}", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().
                        string("{\"name\":\"Milk\",\"costPerOneKilo\":90.0,\"quantityInStock\":3000.0}"));
        verify(ingredientService).findById(id);
    }

    @Test
    void shouldReturnIngredientsDtoByNameStartingWithAndStatus200() throws Exception {
        // given
        String name = "Mi";
        when(ingredientService.findIngredientsByName(name)).thenReturn(new IngredientResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/ingredients/get/{name}", name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"ingredients\":[]}"));
        verify(ingredientService).findIngredientsByName(name);
    }

    @Test
    void shouldAddIngredientAndReturnStatus201() throws Exception {
        // given
        doNothing().when(ingredientService).addIngredient(Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/ingredients/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Ingredient added successfully\"}"));
        verify(ingredientService).addIngredient(Mockito.any());
    }

    @Test
    void shouldUpdateIngredientByIdAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(ingredientService).updateIngredient(Mockito.anyInt(),Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/ingredients/{id}/update", id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
        verify(ingredientService).updateIngredient(Mockito.anyInt(),Mockito.any());
    }

    @Test
    void shouldDeleteIngredientByIdAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(ingredientService).deleteIngredient(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/ingredients/{id}/delete", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
        verify(ingredientService).deleteIngredient(Mockito.anyInt());
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(ingredientController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody() throws JsonProcessingException {
        IngredientDto ingredientDto = new IngredientDto("Milk",90f,3000f);
        return new ObjectMapper().writeValueAsString(ingredientDto);
    }

}