package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceResponse;
import CoffeeApp.storageservice.services.AcceptanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@ExtendWith(MockitoExtension.class)
class AcceptanceControllerTest {

    @Mock
    private AcceptanceService acceptanceService;

    @InjectMocks
    private  AcceptanceController acceptanceController;

    @Test
    void shouldReturnAllAcceptancesDtoAndStatus200() throws Exception {
        // given
        when(acceptanceService.findAll()).thenReturn(new AcceptanceResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/acceptances");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"acceptances\":[]}"));
        verify(acceptanceService).findAll();
    }

    @Test
    void shouldReturnAcceptanceDtoByIdAndStatus200() throws Exception {
        // given
        int acceptanceId = 1;
        AcceptanceDto acceptanceDto = new AcceptanceDto(null,"date","comment",null,1000f);
        when(acceptanceService.findById(Mockito.anyInt())).thenReturn(acceptanceDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/acceptances/{id}", acceptanceId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"id\":null,\"date\":\"date\",\"comment\":\"comment\",\"writeOffs\":null,\"total\":1000.0}"));
        verify(acceptanceService).findById(acceptanceId);
    }

    @Test
    void shouldReturnIngredientsDtoInAcceptance() throws Exception {
        // given
        int id = 1;
        when(acceptanceService.getIngredientByAcceptanceId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/acceptances/{id}/ingredients", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
        verify(acceptanceService).getIngredientByAcceptanceId(Mockito.anyInt());
    }

    @Test
    void shouldReturnItemsDtoInAcceptance() throws Exception {
        // given
        int id = 1;
        when(acceptanceService.getItemsByAcceptanceId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/acceptances/{id}/items", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("[]"));
        verify(acceptanceService).getItemsByAcceptanceId(Mockito.anyInt());
    }

    @Test
    void shouldMakeAcceptanceAndReturnStatus201() throws Exception {
        // given
        doNothing().when(acceptanceService).addAcceptance(Mockito.any(), Mockito.anyMap());
        String requestBody = """
            {
                "name": "Test Acceptance"
            }
            """;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/acceptances/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .param("Ingredient1", "2")
                .param("Ingredient2", "3");

        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Ingredients are accepted\"}"));
    }

    @Test
    void shouldMakeAcceptanceFromFileAndReturnStatus201() throws Exception {
        // given
        doNothing().when(acceptanceService).addAcceptanceFromFile(Mockito.any(), Mockito.anyMap(), Mockito.anyString());
        MockMultipartFile file = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "test content".getBytes());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart("/acceptances/add/file/{surchargeRatio}", "1.5")
                .file(file)
                .param("0", "true")
                .param("1", "false");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"201\",\"statusMsg\":\"Ingredients are accepted\"}"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(acceptanceController)
                .build()
                .perform(requestBuilder);
    }

}