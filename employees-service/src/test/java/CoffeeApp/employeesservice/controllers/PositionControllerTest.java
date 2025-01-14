package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.dto.positionDto.PositionDto;
import CoffeeApp.employeesservice.dto.positionDto.PositionResponse;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.services.PositionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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
class PositionControllerTest {

    @Mock
    private PositionService positionService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PositionController positionController;

    @Test
    void shouldReturnAllPositionsAndStatus200() throws Exception {
        // given
        when(positionService.findAll()).thenReturn(new PositionResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/position");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"positions\":[]}"));
    }

    @Test
    void shouldReturnPositionByIdAndStatus200() throws Exception {
        // given
        int id = 1;
        Position position = new Position("Barista",3000f,0.05f);
        PositionDto positionDto = new PositionDto("Barista",3000f,0.05f);
        when(positionService.findById(Mockito.anyInt())).thenReturn(position);
        when(modelMapper.map(position,PositionDto.class)).thenReturn(positionDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/position/{id}",id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"name\":\"Barista\",\"salary\":3000.0,\"royalty\":0.05}"));

    }

    @Test
    void shouldReturnPositionsByNameStartingWithAndStatus200() throws Exception {
        // given
        String name = "Manager";
        when(positionService.findByName(Mockito.anyString())).thenReturn(new PositionResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/position/get/{name}",name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"positions\":[]}"));
    }

    @Test
    void shouldAddPositionAndReturnStatus201() throws Exception {
        // given
        doNothing().when(positionService).addPosition(Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/position/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Position added successfully\"}"));
    }

    @Test
    void shouldUpdatePositionAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(positionService).updatePosition(Mockito.anyInt(),Mockito.any());
        String requestBody = createRequestBody();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/position/{id}/update",id)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    @Test
    void shouldDeletePositionAndReturnStatus200() throws Exception {
        int id  = 1;
        doNothing().when(positionService).deletePosition(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/position/{id}/delete",id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }


    private String createRequestBody() throws JsonProcessingException {
        PositionDto positionDto = new PositionDto("Barista",3000f,0.05f);
        return new ObjectMapper().writeValueAsString(positionDto);
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(positionController)
                .build()
                .perform(requestBuilder);
    }
}