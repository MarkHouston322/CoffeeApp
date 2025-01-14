package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.dto.salaryDto.SalaryDto;
import CoffeeApp.employeesservice.dto.salaryDto.SalaryResponse;
import CoffeeApp.employeesservice.models.Salary;
import CoffeeApp.employeesservice.services.SalaryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class SalaryControllerTest {

    @Mock
    private SalaryService salaryService;

    @InjectMocks
    private SalaryController salaryController;

    @Test
    void shouldReturnAllSalariesAndStatus200() throws Exception {
        // given
        when(salaryService.findAll()).thenReturn(new SalaryResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/salary");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"salaries\":[]}"));
        verify(salaryService).findAll();
    }

    @Test
    void shouldReturnSalaryByIdAndStatus200() throws Exception {
        // given
        int id = 1;
        SalaryDto salaryDto = new SalaryDto(null,null, 5000f, 0.05f, 0, 5000f, 1000, false);
        when(salaryService.findById(Mockito.anyInt())).thenReturn(salaryDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/salary/{id}", id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"employee\":null,\"date\":null,\"salary\":5000.0,\"royalty\":0.05,\"bonus\":0,\"total\":5000.0,\"revenue\":1000,\"isClosed\":false}"));
        verify(salaryService).findById(id);
    }

    @Test
    void shouldReturnSalaryByEmployeeNameAndStatus200() throws Exception {
        // given
        String employeeName = "Denis";
        when(salaryService.findByEmployeeName(Mockito.anyString())).thenReturn(new SalaryResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/salary/employee/{name}",employeeName);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"salaries\":[]}"));
        verify(salaryService).findByEmployeeName(employeeName);
    }

    @Test
    void shouldDeleteSalaryByIdAndReturnStatus200() throws Exception {
        // given
        int id = 1;
        doNothing().when(salaryService).deleteSalary(Mockito.anyInt());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/salary/{id}/delete",id);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
        verify(salaryService).deleteSalary(id);

    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(salaryController)
                .build()
                .perform(requestBuilder);
    }
}