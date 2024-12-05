package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.categoryDto.CategoryDto;
import CoffeeApp.storageservice.dto.categoryDto.CategoryResponse;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.CategoryAlreadyExistsException;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryDto findById(int id) {
        Category category = checkIfExists(id);
        return convertToCategoryDto(category);
    }

    public CategoryResponse findAll() {
        return new CategoryResponse(categoryRepository.findAll().stream().map(this::convertToCategoryDto)
                .collect(Collectors.toList()));
    }

    public CategoryResponse findCategoriesByName(String query) {
        return new CategoryResponse(categoryRepository.findByNameStartingWith(query).stream().map(this::convertToCategoryDto)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void addCategory(CategoryDto categoryDto) {
        Category categoryToAdd = convertToCategory(categoryDto);
        Optional<Category> optionalCategory =categoryRepository.findByName(categoryToAdd.getName());
        if (optionalCategory.isPresent()){
            throw new CategoryAlreadyExistsException("Category has already been added with this name: " + categoryToAdd.getName());
        }
        categoryRepository.save(categoryToAdd);
    }

    @Transactional
    public boolean deleteCategory(int id) {
        checkIfExists(id);
        categoryRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean updateCategory(int id, CategoryDto categoryDto) {
        boolean isUpdated = false;
        if (categoryDto != null){
            Category categoryToBeUpdated = checkIfExists(id);
            Category updatedCategory = convertToCategory(categoryDto);
            updatedCategory.setId(id);
            updatedCategory.setDrinks(categoryToBeUpdated.getDrinks());
            categoryRepository.save(updatedCategory);
            isUpdated = true;
        }
       return isUpdated;
    }

    private Category checkIfExists(int id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", Integer.toString(id))
        );
    }

    private CategoryDto convertToCategoryDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    private Category convertToCategory(CategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);
    }
}
