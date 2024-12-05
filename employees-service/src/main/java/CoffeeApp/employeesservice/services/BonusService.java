package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.bonusDto.BonusDto;
import CoffeeApp.employeesservice.dto.bonusDto.BonusResponse;
import CoffeeApp.employeesservice.exceptions.BonusAlreadyExistsException;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Bonus;
import CoffeeApp.employeesservice.repositories.BonusRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class BonusService {

    private final BonusRepository bonusRepository;
    private final ModelMapper modelMapper;

    public BonusDto findById(Integer id){
        Bonus bonus = checkIfExists(id);
        return convertToBonusDto(bonus);
    }

    public BonusDto findByEdge(Integer edge){
        Bonus bonus = bonusRepository.findByEdge(edge).orElseThrow(
                () -> new ResourceNotFoundException("Bonus", "edge", Integer.toString(edge))
        );
        return convertToBonusDto(bonus);
    }

    public BonusResponse findAll(){
        return new BonusResponse(bonusRepository.findAll().stream().map(this::convertToBonusDto)
                .collect(Collectors.toList()));
    }

    public Bonus findByRequiredEdge(Integer revenue){
        Optional<Bonus> optionalBonus = bonusRepository.findByRequiredEdge(revenue);
        return optionalBonus.orElseGet(() -> new Bonus(0, 0));
    }

    @Transactional
    public void addBonus(BonusDto bonusDto){
        Bonus bonusToAdd = convertToBonus(bonusDto);
        Optional<Bonus> optionalBonus = bonusRepository.findByEdge(bonusDto.getEdge());
        if (optionalBonus.isPresent()){
            throw new BonusAlreadyExistsException("Bonus has already been added with this edge: " + bonusToAdd.getEdge());
        }
        bonusRepository.save(bonusToAdd);
    }

    @Transactional
    public boolean deleteBonus(Integer id){
        checkIfExists(id);
        bonusRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean updateBonus(Integer id, BonusDto bonusDto){
        boolean isUpdated = false;
        if (bonusDto != null){
            Bonus bonusToBeUpdated = checkIfExists(id);
            Bonus updatedBonus = convertToBonus(bonusDto);
            updatedBonus.setId(bonusToBeUpdated.getId());
            bonusRepository.save(updatedBonus);
            isUpdated = true;
        }
        return isUpdated;
    }


    private Bonus checkIfExists(int id) {
        return bonusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Bonus", "id", Integer.toString(id))
        );
    }

    private Bonus convertToBonus(BonusDto bonusDto){
        return modelMapper.map(bonusDto, Bonus.class);
    }

    private BonusDto convertToBonusDto(Bonus bonus){
        return modelMapper.map(bonus, BonusDto.class);
    }
}
