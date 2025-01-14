package CoffeeApp.customerservice.services;

import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelDto;
import CoffeeApp.customerservice.dto.loyaltyLevelDto.LoyaltyLevelResponse;
import CoffeeApp.customerservice.exceptions.LoyaltyLevelAlreadyExistsException;
import CoffeeApp.customerservice.exceptions.ResourceNotFoundException;
import CoffeeApp.customerservice.models.LoyaltyLevel;
import CoffeeApp.customerservice.repositories.LoyaltyLevelRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class LoyaltyLevelService {

    private final LoyaltyLevelRepository loyaltyLevelRepository;
    private final ModelMapper modelMapper;

    public LoyaltyLevelDto findById(Integer id) {
        LoyaltyLevel loyaltyLevel = checkIfExists(id);
        return convertToLoyaltyLevelDto(loyaltyLevel);
    }

    public LoyaltyLevelResponse findByName(String name) {
        return new LoyaltyLevelResponse(loyaltyLevelRepository.findByNameStartingWith(name).stream().map(this::convertToLoyaltyLevelDto)
                .collect(Collectors.toList()));
    }

    public LoyaltyLevelResponse findAll() {
        return new LoyaltyLevelResponse(loyaltyLevelRepository.findAll().stream().map(this::convertToLoyaltyLevelDto)
                .collect(Collectors.toList()));
    }

    public LoyaltyLevel findByEdge(Integer userPurchase) {
        return loyaltyLevelRepository.findByEdge(userPurchase);
    }

    public LoyaltyLevel findMinLevel() {
        return loyaltyLevelRepository.findMin();
    }

    @Transactional
    public void addLoyaltyLevel(LoyaltyLevelDto loyaltyLevelDto) {
        LoyaltyLevel loyaltyLevelToAdd = convertToLoyaltyLevel(loyaltyLevelDto);
        Optional<LoyaltyLevel> optionalLoyaltyLevel = loyaltyLevelRepository.findByName(loyaltyLevelToAdd.getName());
        if (optionalLoyaltyLevel.isPresent()) {
            throw new LoyaltyLevelAlreadyExistsException("Loyalty level has already been added with this name: " + loyaltyLevelToAdd.getName());
        }
        loyaltyLevelRepository.save(loyaltyLevelToAdd);
    }

    @Transactional
    public void deleteLoyaltyLevel(Integer id) {
        checkIfExists(id);
        loyaltyLevelRepository.deleteById(id);
    }

    @Transactional
    public void updateLoyaltyService(Integer id, LoyaltyLevelDto loyaltyLevelDto) {
        LoyaltyLevel loyaltyLevelToBeUpdated = checkIfExists(id);
        LoyaltyLevel updatedLoyaltyLevel = convertToLoyaltyLevel(loyaltyLevelDto);
        updatedLoyaltyLevel.setId(id);
        updatedLoyaltyLevel.setCustomers(loyaltyLevelToBeUpdated.getCustomers());
        loyaltyLevelRepository.save(updatedLoyaltyLevel);
    }

    private LoyaltyLevel checkIfExists(int id) {
        return loyaltyLevelRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Loyalty level", "id", Integer.toString(id))
        );
    }

    private LoyaltyLevelDto convertToLoyaltyLevelDto(LoyaltyLevel loyaltyLevel) {
        return modelMapper.map(loyaltyLevel, LoyaltyLevelDto.class);
    }

    private LoyaltyLevel convertToLoyaltyLevel(LoyaltyLevelDto loyaltyLevelDto) {
        return modelMapper.map(loyaltyLevelDto, LoyaltyLevel.class);
    }
}
