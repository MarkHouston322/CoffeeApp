package CoffeeApp.financialservice.in.Coffee.application.services;

import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeDto;
import CoffeeApp.financialservice.in.Coffee.application.dto.transactioTypeDto.TransactionTypeResponse;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.ResourceNotFoundException;
import CoffeeApp.financialservice.in.Coffee.application.exceptions.TransactionTypeAlreadyExistsException;
import CoffeeApp.financialservice.in.Coffee.application.models.TransactionType;
import CoffeeApp.financialservice.in.Coffee.application.repositories.TransactionTypeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;
    private final ModelMapper modelMapper;


    public TransactionTypeResponse findAll(){
        return new TransactionTypeResponse(transactionTypeRepository.findAll().stream().map(this::convertToTransactionTypeDto)
                .collect(Collectors.toList()));
    }

    public TransactionTypeDto findById(Integer id){
        TransactionType transactionType = checkIfExists(id);
        return convertToTransactionTypeDto(transactionType);
    }

    public TransactionType findByName(String name){
        return transactionTypeRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Transaction type", "name", name)
        );
    }
    public TransactionTypeResponse findTransactionTypeByName(String name){
        return new TransactionTypeResponse(transactionTypeRepository.findByNameStartingWith(name).stream().map(this::convertToTransactionTypeDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addTransactionType(TransactionTypeDto transactionTypeDto){
        TransactionType transactionType = convertToTransactionType(transactionTypeDto);
        Optional<TransactionType> optionalTransactionType = transactionTypeRepository.findByName(transactionType.getName());
        if (optionalTransactionType.isPresent()){
            throw new TransactionTypeAlreadyExistsException("Transaction type has already been added with this name: " + transactionType.getName());
        }
        transactionTypeRepository.save(transactionType);
    }

    @Transactional
    public boolean updateTransactionalType(Integer id, TransactionTypeDto transactionTypeDto){
        boolean isUpdated = false;
        if (transactionTypeDto != null){
            TransactionType transactionTypeToBeUpdated = checkIfExists(id);
            TransactionType updatedTransactionType = convertToTransactionType(transactionTypeDto);
            updatedTransactionType.setId(id);
            updatedTransactionType.setTransactions(transactionTypeToBeUpdated.getTransactions());
            transactionTypeRepository.save(updatedTransactionType);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Transactional
    public boolean deleteTransactionalType(Integer id){
        checkIfExists(id);
        transactionTypeRepository.deleteById(id);
        return true;
    }
    private TransactionType checkIfExists(int id) {
        return transactionTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Transaction type", "id", Integer.toString(id))
        );
    }

    private TransactionTypeDto convertToTransactionTypeDto(TransactionType transactionType){
        return modelMapper.map(transactionType, TransactionTypeDto.class);
    }

    private TransactionType convertToTransactionType(TransactionTypeDto transactionTypeDto){
        return modelMapper.map(transactionTypeDto, TransactionType.class);
    }
}
