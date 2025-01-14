package CoffeeApp.employeesservice.controllers;

import CoffeeApp.employeesservice.dto.employeeDto.EmployeeDto;
import CoffeeApp.employeesservice.dto.employeeDto.EmployeeResponse;
import CoffeeApp.employeesservice.models.Employee;
import CoffeeApp.employeesservice.services.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void shouldReturnAllEmployeesAndStatus200() throws Exception {
        // given
        when(employeeService.findAll()).thenReturn(new EmployeeResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"employees\":[]}"));
    }

    @Test
    void shouldReturnEmployeeDtoByName() throws Exception {
        // given
        String name = "Test";
        Employee employee = new Employee(name, null);
        EmployeeDto employeeDto = new EmployeeDto(name,null);
        when(employeeService.findByName(Mockito.anyString())).thenReturn(employee);
        when(modelMapper.map(employee,EmployeeDto.class)).thenReturn(employeeDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/get/{name}",name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content()
                        .string("{\"name\":\"Test\",\"position\":null}"));
        verify(employeeService).findByName(name);

    }

    @Test
    void shouldDeleteEmployeeAndReturnStatus200() throws Exception {
        // given
        String name = "Test";
        doNothing().when(employeeService).deleteEmployee(Mockito.anyString());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employee/delete/{name}", name);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));
    }

    @Test
    void shouldAssignPositionAndReturnStatus200() throws Exception {
        Employee employee = new Employee("Test",null);
        int positionId = 1;
        doNothing().when(employeeService).assignPosition(employee.getName(),positionId);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch(
                "/employee/assignPosition/{name}/{positionId}"
                , employee.getName(),positionId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"statusCode\":\"200\",\"statusMsg\":\"Request processed successfully\"}"));

    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);
    }

}