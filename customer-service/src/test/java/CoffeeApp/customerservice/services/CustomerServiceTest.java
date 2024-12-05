package CoffeeApp.customerservice.services;

import CoffeeApp.customerservice.dto.customerDto.AddCustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerResponse;
import CoffeeApp.customerservice.exceptions.CustomerAlreadyExistsException;
import CoffeeApp.customerservice.exceptions.ResourceNotFoundException;
import CoffeeApp.customerservice.models.Customer;
import CoffeeApp.customerservice.models.LoyaltyLevel;
import CoffeeApp.customerservice.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LoyaltyLevelService loyaltyLevelService;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldReturnCustomerDtoByFindingById() {
        // given
        Integer customerId = 1;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setMobileNumber("9998117351");
        customer.setEmail("example@gmail.com");

        CustomerDto expectedDto =  new CustomerDto();
        expectedDto.setMobileNumber("9998117351");
        expectedDto.setEmail("example@gmail.com");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(expectedDto);
        // when
        CustomerDto result = customerService.findById(customerId);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getMobileNumber()).isEqualTo(customer.getMobileNumber());
        assertThat(result.getEmail()).isEqualTo(customer.getEmail());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldNotReturnCustomerDtoByFindingByIdAndThrowException() {
        // given
        Integer customerId = 1;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> customerService.findById(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Customer", "id", Integer.toString(customerId));
        verify(customerRepository).findById(customerId);
    }

    @Test
    void shouldFindCustomersBySecondNamePrefix() {
        // given
        String secondName = "Smith";
        Customer customer1 = new Customer();
        customer1.setSecondName("Smithson");

        Customer customer2 = new Customer();
        customer2.setSecondName("Smithfield");

        List<Customer> customers = Arrays.asList(customer1, customer2);
        when(customerRepository.findBySecondNameStartingWith(secondName)).thenReturn(customers);

        CustomerDto expectedDto1 = new CustomerDto(); // assuming this is correctly mapped
        expectedDto1.setSecondName("Smithson");

        CustomerDto expectedDto2 = new CustomerDto();
        expectedDto2.setSecondName("Smithfield");

        when(modelMapper.map(customer1, CustomerDto.class)).thenReturn(expectedDto1);
        when(modelMapper.map(customer2, CustomerDto.class)).thenReturn(expectedDto2);
        // when
        CustomerResponse response = customerService.findBySecondName(secondName);
        // then
        assertThat(response.getCustomers()).containsExactly(expectedDto1, expectedDto2);
        verify(customerRepository).findBySecondNameStartingWith(secondName);
    }

    @Test
    void shouldNotFindCustomersBySecondNamePrefix(){
        // given
        String surname = "Smith";
        when(customerRepository.findBySecondNameStartingWith(surname)).thenReturn(new ArrayList<>());
        // when
        CustomerResponse response = customerService.findBySecondName(surname);
        // then
        assertThat(response.getCustomers()).isEmpty();
        verify(customerRepository).findBySecondNameStartingWith(surname);
    }

    @Test
    void shouldFindCustomersByMobileNumberEnding(){
        // given
        String mobileNumberEnding = "7351";
        Customer customer1 = new Customer();
        customer1.setMobileNumber("9998117351");

        Customer customer2 = new Customer();
        customer2.setMobileNumber("9996667351");

        List<Customer> customers = Arrays.asList(customer1, customer2);
        when(customerRepository.findByMobileNumberEndingWith(mobileNumberEnding)).thenReturn(customers);

        CustomerDto expectedDto1 = new CustomerDto(); // assuming this is correctly mapped
        expectedDto1.setMobileNumber("9998117351");

        CustomerDto expectedDto2 = new CustomerDto();
        expectedDto2.setMobileNumber("9996667351");

        when(modelMapper.map(customer1, CustomerDto.class)).thenReturn(expectedDto1);
        when(modelMapper.map(customer2, CustomerDto.class)).thenReturn(expectedDto2);
        // when
        CustomerResponse response = customerService.findByMobileNumber(mobileNumberEnding);
        // then
        assertThat(response.getCustomers()).containsExactly(expectedDto1, expectedDto2);
        verify(customerRepository).findByMobileNumberEndingWith(mobileNumberEnding);
    }

    @Test
    void shouldNotFindCustomersByMobileNumberEnding(){
        // given
        String mobileNumberEnding = "7351";
        when(customerRepository.findByMobileNumberEndingWith(mobileNumberEnding)).thenReturn(new ArrayList<>());
        // when
        CustomerResponse response = customerService.findByMobileNumber(mobileNumberEnding);
        // then
        assertThat(response.getCustomers()).isEmpty();
        verify(customerRepository).findByMobileNumberEndingWith(mobileNumberEnding);
    }


    @Test
    void shouldReturnAllCustomers() {
        // when
        customerService.findAll();
        // then
        verify(customerRepository).findAll();
    }

    @Test
    void shouldAddCustomer() {
        // given
        AddCustomerDto customerDto = new AddCustomerDto(
                "Name",
                "SecondName",
                "9998117351",
                "example@gmail.com"
        );

        LoyaltyLevel mockLoyaltyLevel = new LoyaltyLevel();
        when(loyaltyLevelService.findMinLevel()).thenReturn(mockLoyaltyLevel);
        when(customerRepository.findByMobileNumber(customerDto.getMobileNumber())).thenReturn(Optional.empty());
        setModelMapper(customerDto);
        // when
        customerService.addCustomer(customerDto);
        // then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getName()).isEqualTo(customerDto.getName());
        assertThat(capturedCustomer.getSecondName()).isEqualTo(customerDto.getSecondName());
        assertThat(capturedCustomer.getMobileNumber()).isEqualTo(customerDto.getMobileNumber());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerDto.getEmail());
        assertThat(capturedCustomer.getLoyaltyLevel()).isEqualTo(mockLoyaltyLevel);
    }

    @Test
    void shouldNotAddCustomerAndThrowException() {
        // given
        AddCustomerDto customerDto = new AddCustomerDto(
                "Name",
                "SecondName",
                "9998117351",
                "example@gmail.com"
        );
        Customer existingCustomer = new Customer();
        existingCustomer.setMobileNumber(customerDto.getMobileNumber());
        // when
        when(customerRepository.findByMobileNumber(customerDto.getMobileNumber())).thenReturn(Optional.of(existingCustomer));
        setModelMapper(customerDto);
        // then
        assertThatThrownBy(() -> customerService.addCustomer(customerDto))
                .isInstanceOf(CustomerAlreadyExistsException.class)
                .hasMessageContaining("Customer has already been added with this mobile number");
    }

    @Test
    void shouldDeleteCustomerWhenExists() {
        // given
        Integer customerId = 1;
        Customer customer = new Customer();
        customer.setId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).deleteById(customerId);
        // when
        boolean result = customerService.deleteCustomer(customerId);
        // then
        assertThat(result).isTrue();
        verify(customerRepository, times(1)).findById(customerId);

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(customerRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(customerId);
    }

    @Test
    void shouldUpdateCustomer() {
        // given
        Integer customerId = 1;
        AddCustomerDto addCustomerDto = new AddCustomerDto(
                "UpdatedName",
                "UpdatedSecondName",
                "9998117351",
                "newemail@example.com"
        );

        Customer existingCustomer = new Customer();
        existingCustomer.setId(customerId);
        existingCustomer.setName("OriginalName");
        existingCustomer.setSecondName("OriginalSecondName");
        existingCustomer.setMobileNumber("9998117351");
        existingCustomer.setEmail("originalemail@example.com");
        existingCustomer.setLoyaltyLevel(new LoyaltyLevel());
        existingCustomer.setTotalPurchases(10);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        setModelMapper(addCustomerDto);
        // when
        boolean isUpdated = customerService.updateCustomer(customerId, addCustomerDto);

        // then
        assertThat(isUpdated).isTrue();
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(customerCaptor.capture());

        Customer savedCustomer = customerCaptor.getValue();
        assertThat(savedCustomer.getId()).isEqualTo(existingCustomer.getId());
        assertThat(savedCustomer.getName()).isEqualTo(addCustomerDto.getName());
        assertThat(savedCustomer.getSecondName()).isEqualTo(addCustomerDto.getSecondName());
        assertThat(savedCustomer.getMobileNumber()).isEqualTo(addCustomerDto.getMobileNumber());
        assertThat(savedCustomer.getEmail()).isEqualTo(existingCustomer.getEmail());
        assertThat(savedCustomer.getLoyaltyLevel()).isEqualTo(existingCustomer.getLoyaltyLevel());
        assertThat(savedCustomer.getTotalPurchases()).isEqualTo(existingCustomer.getTotalPurchases());
    }

    @Test
    void shouldIncreaseTotalPurchase() {
        // given
        Integer id = 1;
        Integer additionalPurchase = 5;
        LoyaltyLevel currentLoyaltyLevel = new LoyaltyLevel("Standard", 0, 0f);
        currentLoyaltyLevel.setId(1);
        LoyaltyLevel newLoyaltyLevel = new LoyaltyLevel("Premium", 1000, 10f );
        newLoyaltyLevel.setId(2);

        Customer existingCustomer = new Customer();
        existingCustomer.setId(id);
        existingCustomer.setTotalPurchases(10);
        existingCustomer.setLoyaltyLevel(currentLoyaltyLevel);

        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(loyaltyLevelService.findByEdge(existingCustomer.getTotalPurchases() + additionalPurchase))
                .thenReturn(newLoyaltyLevel);

        // when
        customerService.increaseTotalPurchase(id, additionalPurchase);

        // then
        assertThat(existingCustomer.getTotalPurchases()).isEqualTo(15); // Проверяем обновление покупок
        assertThat(existingCustomer.getLoyaltyLevel().getId()).isEqualTo(newLoyaltyLevel.getId()); // Проверяем обновление уровня лояльности

        verify(loyaltyLevelService).findByEdge(existingCustomer.getTotalPurchases());
    }


    private void setModelMapper(AddCustomerDto customerDto){
       when(modelMapper.map(customerDto, Customer.class)).thenReturn(new Customer(customerDto.getName(),
               customerDto.getSecondName(),
               customerDto.getMobileNumber(),
               customerDto.getEmail()));
   }
}