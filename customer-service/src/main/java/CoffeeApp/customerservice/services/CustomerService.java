package CoffeeApp.customerservice.services;

import CoffeeApp.customerservice.dto.customerDto.AddCustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerInOrderDto;
import CoffeeApp.customerservice.dto.customerDto.CustomerResponse;
import CoffeeApp.customerservice.exceptions.CustomerAlreadyExistsException;
import CoffeeApp.customerservice.exceptions.ResourceNotFoundException;
import CoffeeApp.customerservice.models.Customer;
import CoffeeApp.customerservice.models.LoyaltyLevel;
import CoffeeApp.customerservice.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final LoyaltyLevelService loyaltyLevelService;
    private final StreamBridge streamBridge;

    public CustomerDto findById(Integer id) {
        Customer customer = checkIfExists(id);
        return convertToCustomerDto(customer);
    }

    public CustomerResponse findBySecondName(String secondName) {
        return new CustomerResponse(customerRepository.findBySecondNameStartingWith(secondName).stream().map(this::convertToCustomerDto)
                .collect(Collectors.toList()));
    }

    public CustomerResponse findByMobileNumber(String mobileNumber) {
        return new CustomerResponse(customerRepository.findByMobileNumberEndingWith(mobileNumber).stream().map(this::convertToCustomerDto)
                .collect(Collectors.toList()));
    }

    public CustomerResponse findAll() {
        return new CustomerResponse(customerRepository.findAll().stream().map(this::convertToCustomerDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addCustomer(AddCustomerDto addCustomerDto) {
        Customer customerToAdd = convertToCustomer(addCustomerDto);
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerToAdd.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer has already been added with this mobile number: " + customerToAdd.getMobileNumber());
        }
        customerToAdd.setTotalPurchases(1);
        customerToAdd.setLoyaltyLevel(loyaltyLevelService.findMinLevel());
        customerRepository.save(customerToAdd);
        sendCustomer(customerToAdd, customerToAdd.getLoyaltyLevel());
    }

    @Transactional
    public void deleteCustomer(Integer id) {
        checkIfExists(id);
        customerRepository.deleteById(id);
    }

    @Transactional
    public void updateCustomer(Integer id, AddCustomerDto addCustomerDto) {
        Customer customerToBeUpdated = checkIfExists(id);
        Customer updatedCustomer = convertToCustomer(addCustomerDto);
        updatedCustomer.setId(customerToBeUpdated.getId());
        updatedCustomer.setEmail(customerToBeUpdated.getEmail());
        updatedCustomer.setLoyaltyLevel(customerToBeUpdated.getLoyaltyLevel());
        updatedCustomer.setTotalPurchases(customerToBeUpdated.getTotalPurchases());
        customerRepository.save(updatedCustomer);

    }

    @Transactional
    public void increaseTotalPurchase(Integer id, Integer purchase) {
        Customer customer = checkIfExists(id);
        Integer userPurchases = customer.getTotalPurchases() + purchase;
        customer.setTotalPurchases(userPurchases);
        LoyaltyLevel loyaltyLevel = loyaltyLevelService.findByEdge(userPurchases);
        if (!Objects.equals(customer.getLoyaltyLevel().getId(), loyaltyLevel.getId())) {
            customer.setLoyaltyLevel(loyaltyLevel);
            sendCustomer(customer, loyaltyLevel);
        }
    }

    private Customer checkIfExists(int id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "id", Integer.toString(id))
        );
    }

    private void sendCustomer(Customer customer, LoyaltyLevel loyaltyLevel) {
        CustomerInOrderDto customerInOrderDto = new CustomerInOrderDto(customer.getId(), loyaltyLevel.getDiscountPercentage());
        streamBridge.send("sendCustomer-out-0", customerInOrderDto);
    }

    private CustomerDto convertToCustomerDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    private Customer convertToCustomer(AddCustomerDto addCustomerDto) {
        return modelMapper.map(addCustomerDto, Customer.class);
    }
}
