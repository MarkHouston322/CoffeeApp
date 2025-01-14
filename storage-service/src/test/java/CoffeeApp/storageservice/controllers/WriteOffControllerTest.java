package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.dto.writeOffDto.AddWriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffResponse;
import CoffeeApp.storageservice.models.Acceptance;
import CoffeeApp.storageservice.services.WriteOffService;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@ExtendWith(MockitoExtension.class)
class WriteOffControllerTest {

    @Mock
    private WriteOffService writeOffService;

    @InjectMocks
    private WriteOffController writeOffController;

    @Test
    void shouldReturnAllWriteOffsAndStatus200() throws Exception {
        // given
        when(writeOffService.findAll()).thenReturn(new WriteOffResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/write-offs");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"writeOffs\":[]}"));
    }

    @Test
    void shouldReturnWriteOffsDtoByIdAndStatus200() throws Exception {
        // given
        int bonusId = 1;
        WriteOffDto writeOffDto = new WriteOffDto(1,"Date",new Acceptance(),"Reason",1000f);
        when(writeOffService.findById(Mockito.anyInt())).thenReturn(writeOffDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/write-offs/{id}", bonusId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"id\":1,\"date\":\"Date\"," +
                                "\"acceptance\":{\"id\":null,\"date\":null,\"items\":null}," +
                                "\"reason\":\"Reason\",\"total\":1000.0}"));
        verify(writeOffService).findById(bonusId);
    }

    @Test
    void shouldReturnIngredientsDtoInWriteOff() throws Exception {
        // given
        int id = 1;
        when(writeOffService.getIngredientsByWriteOffId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/write-offs/{id}/ingredients", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
        verify(writeOffService).getIngredientsByWriteOffId(Mockito.anyInt());
    }

    @Test
    void shouldReturnItemsDtoInWriteOff() throws Exception {
        // given
        int id = 1;
        when(writeOffService.getItemsByWriteOffId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/write-offs/{id}/items", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
        verify(writeOffService).getItemsByWriteOffId(Mockito.anyInt());
    }

    @Test
    void shouldMakeWriteOffAndReturnStatus201() throws Exception {
        // given
        doNothing().when(writeOffService).addWriteOffGoods(Mockito.any(), Mockito.anyMap());
        String requestBody = new ObjectMapper().writeValueAsString(new AddWriteOffDto("Reason",new Acceptance()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/write-offs/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .param("Ingredient1", "2")
                .param("Ingredient2", "3");

        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Ingredients have been written off\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(writeOffController)
                .build()
                .perform(requestBuilder);
    }

}