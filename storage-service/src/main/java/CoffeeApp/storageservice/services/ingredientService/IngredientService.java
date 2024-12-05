package CoffeeApp.storageservice.services.ingredientService;

import CoffeeApp.storageservice.dto.ingredientDto.IngredientDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientResponse;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.IngredientAlreadyExistsException;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final ModelMapper modelMapper;

    public IngredientDto findById(int id) {
        Ingredient ingredient = checkIfExists(id);
        return convertToIngredientDto(ingredient);
    }

    @Transactional(noRollbackFor = ResourceNotFoundException.class)
    public Ingredient findByName(String name) {
        return ingredientRepository.findByName(name).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "name", name)
        );
    }

    public IngredientResponse findIngredientsByName(String name) {
        return new IngredientResponse(ingredientRepository.findByNameStartingWith(name).stream().map(this::convertToIngredientDto)
                .collect(Collectors.toList()));
    }

    public IngredientResponse findAll() {
        return new IngredientResponse(ingredientRepository.findAll().stream().map(this::convertToIngredientDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addIngredient(IngredientDto ingredientDto) {
        Ingredient ingredientToAdd = convertToIngredient(ingredientDto);
        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(ingredientToAdd.getName());
        if (optionalIngredient.isPresent()) {
            throw new IngredientAlreadyExistsException("Ingredient has already been added with this name: " + ingredientToAdd.getName());
        }
        ingredientRepository.save(ingredientToAdd);
    }

    @Transactional
    public boolean deleteIngredient(int id) {
        checkIfExists(id);
        ingredientRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean updateIngredient(int id, IngredientDto ingredientDto) {
        boolean isUpdated = false;
        if (ingredientDto != null) {
            Ingredient ingredientToBeUpdated = checkIfExists(id);
            Ingredient updatedIngredient = convertToIngredient(ingredientDto);
            updatedIngredient.setId(id);
            updatedIngredient.setName(ingredientToBeUpdated.getName());
            updatedIngredient.setDrinks(ingredientToBeUpdated.getDrinks());
            ingredientRepository.save(updatedIngredient);
            isUpdated = true;
        }
        return isUpdated;
    }


    @Transactional
    public void decreaseIngredient(Integer id, float decreaseQuantity) {
        ingredientRepository.findById(id).ifPresent(
                ingredient -> ingredient.setQuantityInStock(ingredient.getQuantityInStock() - decreaseQuantity)
        );
    }

    @Transactional
    public void decreaseIngredient(String name, float decreaseQuantity) {
        ingredientRepository.findByName(name).ifPresent(
                ingredient -> ingredient.setQuantityInStock(ingredient.getQuantityInStock() - decreaseQuantity)
        );
    }


    @Transactional
    public void increaseIngredients(String name, float increaseQuantity) {
        ingredientRepository.findByName(name).ifPresent(
                ingredient -> ingredient.setQuantityInStock(ingredient.getQuantityInStock() + increaseQuantity)
        );
    }


    private Ingredient checkIfExists(int id) {
        return ingredientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Ingredient", "id", Integer.toString(id))
        );
    }

    private IngredientDto convertToIngredientDto(Ingredient ingredient) {
        return modelMapper.map(ingredient, IngredientDto.class);
    }

    private Ingredient convertToIngredient(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, Ingredient.class);
    }

    public float calculateCost(Map<String, String> ingredients) {
        float total = 0;

        for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            Ingredient ingredient = findByName(entry.getKey());
            float quantity = Float.parseFloat(entry.getValue());

            float ingredientCost = ingredient.getCostPerOneKilo() * quantity;
            total += ingredientCost;
        }
        return total;
    }

}