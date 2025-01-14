package CoffeeApp.sellingservice.services;

import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodResponse;
import CoffeeApp.sellingservice.exceptions.PaymentMethodAlreadyExistsException;
import CoffeeApp.sellingservice.exceptions.ResourceNotFoundException;
import CoffeeApp.sellingservice.models.PaymentMethod;
import CoffeeApp.sellingservice.repositories.PaymentMethodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PaymentMethodService paymentMethodService;

    @Test
    void shouldFindPaymentMethodDtoById(){
        // given
        PaymentMethod paymentMethod = new PaymentMethod("Cash");
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto("Cash");
        when(paymentMethodRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(paymentMethod));
        when(modelMapper.map(paymentMethod,PaymentMethodDto.class)).thenReturn(paymentMethodDto);
        // when
        PaymentMethodDto result = paymentMethodService.findById(Mockito.anyInt());
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(paymentMethod.getName());
        verify(paymentMethodRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotFindPaymentMethodDtoById(){
        // given
        int id = 1;
        when(paymentMethodRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> paymentMethodService.findById(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Payment method", "id", Integer.toString(id));
        verify(paymentMethodRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldReturnPaymentMethodsDtoByNameStartingWith(){
        // given
        PaymentMethod paymentMethod1 = new PaymentMethod("Cash");
        PaymentMethod paymentMethod2 = new PaymentMethod("Card");
        List<PaymentMethod> methods = Arrays.asList(paymentMethod1,paymentMethod2);
        PaymentMethodDto paymentMethodDto1 = new PaymentMethodDto("Cash");
        PaymentMethodDto paymentMethodDto2 = new PaymentMethodDto("Card");
        when(paymentMethodRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(methods);
        when(modelMapper.map(paymentMethod1,PaymentMethodDto.class)).thenReturn(paymentMethodDto1);
        when(modelMapper.map(paymentMethod2,PaymentMethodDto.class)).thenReturn(paymentMethodDto2);
        // when
        PaymentMethodResponse response = paymentMethodService.findByName(Mockito.anyString());
        // then
        assertThat(response.getPaymentMethods()).containsExactly(paymentMethodDto1,paymentMethodDto2);
        verify(paymentMethodRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldNotReturnPaymentMethodsDtoByNameStartingWith(){
        // given
        when(paymentMethodRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(Collections.emptyList());
        // when
        PaymentMethodResponse response = paymentMethodService.findByName(Mockito.anyString());
        // then
        assertThat(response.getPaymentMethods()).isEmpty();
        verify(paymentMethodRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldReturnAllPaymentMethodsDto(){
        // given
        PaymentMethod paymentMethod1 = new PaymentMethod("Cash");
        PaymentMethod paymentMethod2 = new PaymentMethod("Card");
        List<PaymentMethod> methods = Arrays.asList(paymentMethod1,paymentMethod2);
        PaymentMethodDto paymentMethodDto1 = new PaymentMethodDto("Cash");
        PaymentMethodDto paymentMethodDto2 = new PaymentMethodDto("Card");
        when(paymentMethodRepository.findAll()).thenReturn(methods);
        when(modelMapper.map(paymentMethod1,PaymentMethodDto.class)).thenReturn(paymentMethodDto1);
        when(modelMapper.map(paymentMethod2,PaymentMethodDto.class)).thenReturn(paymentMethodDto2);
        // when
        PaymentMethodResponse response = paymentMethodService.findAll();
        // then
        assertThat(response.getPaymentMethods()).containsExactly(paymentMethodDto1,paymentMethodDto2);
        verify(paymentMethodRepository).findAll();
    }

    @Test
    void shouldNotReturnAllPaymentMethodsDto(){
        // given
        when(paymentMethodRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        PaymentMethodResponse response = paymentMethodService.findAll();
        // then
        assertThat(response.getPaymentMethods()).isEmpty();
        verify(paymentMethodRepository).findAll();
    }

    @Test
    void shouldAddPaymentMethod(){
        // given
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto("Cash");
        when(paymentMethodRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(paymentMethodDto,PaymentMethod.class)).thenReturn(new PaymentMethod(paymentMethodDto.getName()));
        // when
        paymentMethodService.addPaymentMethod(paymentMethodDto);
        // then
        ArgumentCaptor<PaymentMethod> methodCaptor = ArgumentCaptor.forClass(PaymentMethod.class);
        verify(paymentMethodRepository).save(methodCaptor.capture());
        PaymentMethod paymentMethod = methodCaptor.getValue();
        assertThat(paymentMethod.getName()).isEqualTo(paymentMethodDto.getName());
    }

    @Test
    void shouldNotAddPaymentMethod(){
        // given
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto("Cash");
        PaymentMethod existingMethod = new PaymentMethod("Cash");
        when(paymentMethodRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(existingMethod));
        when(modelMapper.map(paymentMethodDto,PaymentMethod.class)).thenReturn(new PaymentMethod(paymentMethodDto.getName()));
        // when & then
        assertThatThrownBy(() -> paymentMethodService.addPaymentMethod(paymentMethodDto))
                .isInstanceOf(PaymentMethodAlreadyExistsException.class)
                .hasMessageContaining("Payment method has already been added with this name");
        verify(paymentMethodRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldDeletePaymentMethodById(){
        // given
        int id = 1;
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(id);
        when(paymentMethodRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(paymentMethod));
        doNothing().when(paymentMethodRepository).deleteById(Mockito.anyInt());
        // when
        paymentMethodService.deletePaymentMethod(id);
        // then
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(paymentMethodRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
        verify(paymentMethodRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldUpdatePaymentMethodById(){
        // given
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto("Card");
        PaymentMethod existingMethod = new PaymentMethod("Cash");
        when(paymentMethodRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(existingMethod));
        when(modelMapper.map(paymentMethodDto,PaymentMethod.class)).thenReturn(new PaymentMethod(paymentMethodDto.getName()));
        // when
        paymentMethodService.updatePaymentMethod(Mockito.anyInt(),paymentMethodDto);
        // then
        ArgumentCaptor<PaymentMethod> methodCapture = ArgumentCaptor.forClass(PaymentMethod.class);
        verify(paymentMethodRepository).save(methodCapture.capture());
        PaymentMethod result = methodCapture.getValue();
        assertThat(result.getName()).isEqualTo(paymentMethodDto.getName());
        verify(paymentMethodRepository).findById(Mockito.anyInt());
    }

}