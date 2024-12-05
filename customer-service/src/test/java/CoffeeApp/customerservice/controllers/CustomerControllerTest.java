package CoffeeApp.customerservice.controllers;

import CoffeeApp.customerservice.dto.customerDto.AddCustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerResponse;
import CoffeeApp.customerservice.services.CustomerService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void shouldReturnAllCustomersAndStatus200() throws Exception {
        //given
        when(customerService.findAll()).thenReturn(new CustomerResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers");
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("<CustomerResponse><customers/></CustomerResponse>"));
    }

    @Test
    void shouldReturnCustomerDtoByIdAndStatus200() throws Exception {
        // given
        Integer customerId = 1;
        CustomerDto customerDto = new CustomerDto();
        when(customerService.findById(Mockito.anyInt())).thenReturn(customerDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers/{id}", customerId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("<CustomerDto><name/><secondName/><mobileNumber/><email/><totalPurchases/><loyaltyLevel/></CustomerDto>"));
    }


    @Test
    void shouldReturnCustomersBySecondNameOrMobileNumberAndStatus200() throws Exception {
        // given
        String query = "Query";
        when(customerService.findBySecondName(Mockito.anyString())).thenReturn(new CustomerResponse(new ArrayList<>()));
        when(customerService.findByMobileNumber(Mockito.anyString())).thenReturn(new CustomerResponse(new ArrayList<>()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers/get/{query}", query);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("<Map>" +
                        "<Users find by second name><customers/></Users find by second name>" +
                        "<Users find by mobile number><customers/></Users find by mobile number></Map>"));
    }

    @Test
    void shouldAddCustomerAndReturnStatus201() throws Exception {
        // given
        doNothing().when(customerService).addCustomer(Mockito.<AddCustomerDto>any());
        AddCustomerDto customerDto = new AddCustomerDto("Name", "Surname", "9998117351", "test@gmail.com");
        String requestBody = new ObjectMapper().writeValueAsString(customerDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/customers/add")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("<ResponseDto><statusCode>201</statusCode><statusMsg>Customer added successfully</statusMsg><"
                        + "/ResponseDto>"));
    }

    @Test
    void shouldUpdateCustomerAndReturnStatus200() throws Exception {
        // given
        when(customerService.updateCustomer(Mockito.anyInt(), Mockito.<AddCustomerDto>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = setRequestBuilderForUpdate();
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("<ResponseDto><statusCode>200</statusCode><statusMsg>Request processed successfully</statusMsg><"
                        + "/ResponseDto>"));

    }

    @Test
    void shouldNotUpdateCustomerAndReturnStatus417() throws Exception {
        // given
        when(customerService.updateCustomer(Mockito.anyInt(), Mockito.<AddCustomerDto>any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = setRequestBuilderForUpdate();
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().string("<ResponseDto><statusCode>417</statusCode><statusMsg>Update operation failed. Please try again or contact"
                        + " Dev team</statusMsg></ResponseDto>"));
    }

    @Test
    void shouldDeleteCustomerAndReturnStatus200() throws Exception {
        // given
        Integer customerId = 1;
        when(customerService.deleteCustomer(Mockito.anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/customers/{id}/delete", customerId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("<ResponseDto><statusCode>200</statusCode><statusMsg>Request processed successfully</statusMsg><"
                        + "/ResponseDto>"));
    }

    @Test
    void shouldNotDeleteCustomerAndReturnStatus417() throws Exception {
        // given
        Integer customerId = 1;
        when(customerService.deleteCustomer(Mockito.anyInt())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/customers/{id}/delete", customerId);
        // when & then
        buildRequestBuilder(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(417))
                .andExpect(MockMvcResultMatchers.content().string("<ResponseDto><statusCode>417</statusCode><statusMsg>Delete operation failed. Please try again or contact"
                        + " Dev team</statusMsg></ResponseDto>"));
    }

    private ResultActions buildRequestBuilder(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return MockMvcBuilders.standaloneSetup(customerController)
                .build()
                .perform(requestBuilder);
    }

    private MockHttpServletRequestBuilder setRequestBuilderForUpdate() throws JsonProcessingException {
        AddCustomerDto customerDto = new AddCustomerDto("Name", "Surname", "9998117351", "test@gmail.com");
        String requestBody = new ObjectMapper().writeValueAsString(customerDto);
        return MockMvcRequestBuilders
                .patch("/customers/{id}/update", 1)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
    }

}
