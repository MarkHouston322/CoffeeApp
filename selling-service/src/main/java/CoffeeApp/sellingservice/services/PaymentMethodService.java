package CoffeeApp.sellingservice.services;

import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodResponse;
import CoffeeApp.sellingservice.exceptions.PaymentMethodAlreadyExistsException;
import CoffeeApp.sellingservice.exceptions.ResourceNotFoundException;
import CoffeeApp.sellingservice.models.PaymentMethod;
import CoffeeApp.sellingservice.repositories.PaymentMethodRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;

    public PaymentMethodDto findById(Integer id) {
        PaymentMethod paymentMethod = checkIfExists(id);
        return convertToPaymentMethodDto(paymentMethod);
    }

    public PaymentMethodResponse findByName(String name) {
        return new PaymentMethodResponse(paymentMethodRepository.findByNameStartingWith(name).stream().map(this::convertToPaymentMethodDto)
                .collect(Collectors.toList()));
    }

    public PaymentMethodResponse findAll() {
        return new PaymentMethodResponse(paymentMethodRepository.findAll().stream().map(this::convertToPaymentMethodDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addPaymentMethod(PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethod = convertToPaymentMethod(paymentMethodDto);
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findByName(paymentMethod.getName());
        if (optionalPaymentMethod.isPresent()) {
            throw new PaymentMethodAlreadyExistsException("Payment method has already been added with this name: " + paymentMethod.getName());
        }
        paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public void updatePaymentMethod(Integer id, PaymentMethodDto paymentMethodDto) {
        PaymentMethod paymentMethodToBeUpdated = checkIfExists(id);
        PaymentMethod updatedPaymentMethod = convertToPaymentMethod(paymentMethodDto);
        updatedPaymentMethod.setId(id);
        updatedPaymentMethod.setOrders(paymentMethodToBeUpdated.getOrders());
        paymentMethodRepository.save(updatedPaymentMethod);

    }

    @Transactional
    public void deletePaymentMethod(Integer id) {
        checkIfExists(id);
        paymentMethodRepository.deleteById(id);
    }

    private PaymentMethod checkIfExists(int id) {
        return paymentMethodRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Payment method", "id", Integer.toString(id))
        );
    }

    private PaymentMethodDto convertToPaymentMethodDto(PaymentMethod paymentMethod) {
        return modelMapper.map(paymentMethod, PaymentMethodDto.class);
    }

    private PaymentMethod convertToPaymentMethod(PaymentMethodDto paymentMethodDto) {
        return modelMapper.map(paymentMethodDto, PaymentMethod.class);
    }
}
