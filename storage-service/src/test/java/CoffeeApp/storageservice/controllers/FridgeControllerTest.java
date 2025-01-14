package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.dto.itemDto.AddItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeResponse;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.repositories.itemRepository.ItemInFridgeRepository;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
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
class FridgeControllerTest {

    @Mock
    private ItemInFridgeService itemInFridgeService;

    @InjectMocks
    private FridgeController fridgeController;


    @Test
    void shouldReturnItemsInFridgeAndStatus200() throws Exception {
        // given
        when(itemInFridgeService.getItemsFromFridge()).thenReturn(new ItemInFridgeResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/fridge");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"items\":[]}"));
    }

    @Test
    void shouldReturnAllRecordsInFridgeAndStatus200() throws Exception{
        // given
        when(itemInFridgeService.findAll()).thenReturn(new ItemInFridgeResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/fridge/history");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"items\":[]}"));
    }

    @Test
    void shouldReturnItemHistoryInFridgeAndStatus200() throws Exception {
        // given
        String itemName = "Water";
        when(itemInFridgeService.findByItemName(Mockito.anyString())).thenReturn(new ItemInFridgeResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/fridge/item/{name}", itemName);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"items\":[]}"));
    }

    @Test
    void shouldAddItemInFridgeAndReturnStatus201() throws Exception{
        // given
        doNothing().when(itemInFridgeService).addItemToFridge(Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/fridge/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Item in fridge added successfully\"}"));


    }

    @Test
    void shouldRemoveItemFromFridgeAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(itemInFridgeService).removeItemFromFridge(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/fridge/remove/{id}", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(fridgeController)
                .build()
                .perform(requestBuilder);
    }

    private String createRequestBody() throws JsonProcessingException {
        AddItemInFridgeDto addItemInFridgeDto = new AddItemInFridgeDto(new Item(), 1000L);
        return  new ObjectMapper().writeValueAsString(addItemInFridgeDto);
    }

}