package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.dto.categoryDto.CategoryDto;
import CoffeeApp.storageservice.dto.categoryDto.CategoryResponse;
import CoffeeApp.storageservice.services.CategoryService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void shouldReturnAllCategoriesDtoAndStatus200() throws Exception {
        // given
        when(categoryService.findAll()).thenReturn(new CategoryResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"categories\":[]}"));
        verify(categoryService).findAll();
    }

    @Test
    void shouldReturnCategoryDtoByIdAndStatus200() throws Exception {
        // given
        int id = 1;
        CategoryDto categoryDto = new CategoryDto("Drinks");
        when(categoryService.findById(Mockito.anyInt())).thenReturn(categoryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories/{id}", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"name\":\"Drinks\"}"));
        verify(categoryService).findById(id);
    }

    @Test
    void shouldReturnCategoriesDtoByNameStartingWithAndStatus200() throws Exception {
        // given
        String name = "Drinks";
        when(categoryService.findCategoriesByName(Mockito.anyString())).thenReturn(new CategoryResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/categories/get/{name}",name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"categories\":[]}"));
        verify(categoryService).findCategoriesByName(Mockito.anyString());
    }

    @Test
    void shouldAddCategoryAndReturnStatus201() throws Exception {
        // given
        doNothing().when(categoryService).addCategory(Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/categories/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Category added successfully\"}"));
    }

    @Test
    void shouldUpdateCategoryAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(categoryService).updateCategory(Mockito.anyInt(),Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/categories/{id}/update", id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    @Test
    void shouldDeleteCategoryAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(categoryService).deleteCategory(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/categories/{id}/delete", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(categoryController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody() throws JsonProcessingException {
        CategoryDto categoryDto = new CategoryDto("Drinks");
        return new ObjectMapper().writeValueAsString(categoryDto);
    }

}