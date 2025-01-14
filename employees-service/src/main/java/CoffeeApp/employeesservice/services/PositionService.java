package CoffeeApp.employeesservice.services;

import CoffeeApp.employeesservice.dto.positionDto.PositionDto;
import CoffeeApp.employeesservice.dto.positionDto.PositionResponse;
import CoffeeApp.employeesservice.exceptions.PositionAlreadyExistsException;
import CoffeeApp.employeesservice.exceptions.ResourceNotFoundException;
import CoffeeApp.employeesservice.models.Position;
import CoffeeApp.employeesservice.repositories.PositionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;

    public Position findById(Integer id) {
        return checkIfExists(id);
    }

    public PositionResponse findByName(String name) {
        return new PositionResponse(positionRepository.findByNameStartingWith(name).stream().map(this::convertToPositionDto)
                .collect(Collectors.toList()));
    }

    public PositionResponse findAll() {
        return new PositionResponse(positionRepository.findAll().stream().map(this::convertToPositionDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addPosition(PositionDto positionDto) {
        Position positionToAdd = convertToPosition(positionDto);
        Optional<Position> optionalPosition = positionRepository.findByName(positionToAdd.getName());
        if (optionalPosition.isPresent()) {
            throw new PositionAlreadyExistsException("Position has already been added with this name: " + positionToAdd.getName());
        }
        positionRepository.save(positionToAdd);
    }

    @Transactional
    public void deletePosition(Integer id) {
        checkIfExists(id);
        positionRepository.deleteById(id);
    }

    @Transactional
    public void updatePosition(Integer id, PositionDto positionDto) {
        Position positionToBeUpdated = checkIfExists(id);
        Position updatedPosition = convertToPosition(positionDto);
        updatedPosition.setId(positionToBeUpdated.getId());
        updatedPosition.setEmployees(positionToBeUpdated.getEmployees());
        positionRepository.save(updatedPosition);
    }

    private Position checkIfExists(int id) {
        return positionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Position", "id", Integer.toString(id))
        );
    }

    private Position convertToPosition(PositionDto positionDto) {
        return modelMapper.map(positionDto, Position.class);
    }

    private PositionDto convertToPositionDto(Position position) {
        return modelMapper.map(position, PositionDto.class);
    }
}
