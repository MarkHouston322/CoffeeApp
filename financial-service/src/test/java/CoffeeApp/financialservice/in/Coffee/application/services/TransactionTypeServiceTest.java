package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.TransactionTypeAlreadyExistsException;
import CoffeeApp.financialservice.in.Coffee.application.models.TransactionType;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TransactionTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionTypeServiceTest {

    @Mock
    private TransactionTypeRepository transactionTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionTypeService transactionTypeService;

    @Test
    void shouldReturnAllTransactionTypesDto(){
        // given
        TransactionType type1 = new TransactionType("Cash");
        TransactionType type2 = new TransactionType("Card");
        List<TransactionType> types = Arrays.asList(type1,type2);
        TransactionTypeDto typeDto1 = new TransactionTypeDto("Cash");
        TransactionTypeDto typeDto2 = new TransactionTypeDto("Card");
        when(transactionTypeRepository.findAll()).thenReturn(types);
        when(modelMapper.map(type1, TransactionTypeDto.class)).thenReturn(typeDto1);
        when(modelMapper.map(type2, TransactionTypeDto.class)).thenReturn(typeDto2);
        // when
        TransactionTypeResponse response = transactionTypeService.findAll();
        // then
        assertThat(response.getTransactionTypes()).containsExactly(typeDto1,typeDto2);
        verify(transactionTypeRepository).findAll();
    }

    @Test
    void shouldNotReturnAllTransactionTypesDto(){
        // given
        when(transactionTypeRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        TransactionTypeResponse response = transactionTypeService.findAll();
        // then
        assertThat(response.getTransactionTypes()).isEmpty();
        verify(transactionTypeRepository).findAll();
    }

    @Test
    void shouldReturnTransactionTypesDtoByNameStartingWith(){
        // given
        TransactionType type1 = new TransactionType("Cash");
        TransactionType type2 = new TransactionType("Card");
        List<TransactionType> types = Arrays.asList(type1,type2);
        TransactionTypeDto typeDto1 = new TransactionTypeDto("Cash");
        TransactionTypeDto typeDto2 = new TransactionTypeDto("Card");
        when(transactionTypeRepository.findByNameStartingWith("Ca")).thenReturn(types);
        when(modelMapper.map(type1, TransactionTypeDto.class)).thenReturn(typeDto1);
        when(modelMapper.map(type2, TransactionTypeDto.class)).thenReturn(typeDto2);
        // when
        TransactionTypeResponse response = transactionTypeService.findTransactionTypeByName("Ca");
        // then
        assertThat(response.getTransactionTypes()).containsExactly(typeDto1,typeDto2);
        verify(transactionTypeRepository).findByNameStartingWith("Ca");
    }

    @Test
    void shouldNotReturnTransactionTypesDtoByNameStartingWith(){
        // given
        when(transactionTypeRepository.findByNameStartingWith("Ca")).thenReturn(Collections.emptyList());
        // when
        TransactionTypeResponse response = transactionTypeService.findTransactionTypeByName("Ca");
        // then
        assertThat(response.getTransactionTypes()).isEmpty();
        verify(transactionTypeRepository).findByNameStartingWith("Ca");
    }

    @Test
    void shouldReturnTransactionTypeDtoByName(){
        // given
        String name = "Cash";
        TransactionType type = new TransactionType(name);
        when(transactionTypeRepository.findByName(name)).thenReturn(Optional.of(type));
        // when
        TransactionType result = transactionTypeService.findByName(name);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(type.getName());
        verify(transactionTypeRepository).findByName(name);
    }

    @Test
    void shouldNotReturnTransactionTypeDtoByName(){
        // given
        String name = "Cash";
        when(transactionTypeRepository.findByName(name)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> transactionTypeService.findByName(name))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Transaction type", "name", name);
        verify(transactionTypeRepository).findByName(name);
    }

    @Test
    void shouldAddTransactionType(){
        // given
        TransactionTypeDto typeDto = new TransactionTypeDto("Cash");
        when(transactionTypeRepository.findByName(typeDto.getName())).thenReturn(Optional.empty());
        when(modelMapper.map(typeDto,TransactionType.class)).thenReturn(new TransactionType(typeDto.getName()));
        // then
        transactionTypeService.addTransactionType(typeDto);
        // when
        ArgumentCaptor<TransactionType> typeCaptor = ArgumentCaptor.forClass(TransactionType.class);
        verify(transactionTypeRepository).save(typeCaptor.capture());
        TransactionType type = typeCaptor.getValue();
        assertThat(type.getName()).isEqualTo(typeDto.getName());
    }

    @Test
    void shouldNotAddTransactionType(){
        // given
        TransactionTypeDto typeDto = new TransactionTypeDto("Cash");
        TransactionType existingType = new TransactionType(typeDto.getName());
        when(transactionTypeRepository.findByName(typeDto.getName())).thenReturn(Optional.of(existingType));
        when(modelMapper.map(typeDto,TransactionType.class)).thenReturn(new TransactionType(typeDto.getName()));
        // when & then
        assertThatThrownBy(() -> transactionTypeService.addTransactionType(typeDto))
                .isInstanceOf(TransactionTypeAlreadyExistsException.class)
                .hasMessageContaining("Transaction type has already been added with this name");
    }
}