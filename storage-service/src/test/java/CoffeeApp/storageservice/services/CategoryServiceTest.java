package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.categoryDto.CategoryDto;
import CoffeeApp.storageservice.dto.categoryDto.CategoryResponse;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.CategoryAlreadyExistsException;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.repositories.CategoryRepository;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnCategoryDtoById(){
        // given
        Category category = new Category("Drink");
        CategoryDto categoryDto = new CategoryDto("Drink");
        when(categoryRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(category));
        when(modelMapper.map(category,CategoryDto.class)).thenReturn(categoryDto);
        // when
        CategoryDto result = categoryService.findById(Mockito.anyInt());
        // then
        assertThat(result.getName()).isEqualTo(category.getName());
        verify(categoryRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnCategoryDtoById(){
        // given
        int id = 1;
        when(categoryRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> categoryService.findById(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category", "id", Integer.toString(id));
        verify(categoryRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldReturnAllCategoriesDto(){
        // given
        Category category1 = new Category("Drinks");
        Category category2 = new Category("Desserts");
        List<Category> categories = Arrays.asList(category1,category2);
        CategoryDto categoryDto1 = new CategoryDto("Drinks");
        CategoryDto categoryDto2 = new CategoryDto("Desserts");
        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(category1,CategoryDto.class)).thenReturn(categoryDto1);
        when(modelMapper.map(category2,CategoryDto.class)).thenReturn(categoryDto2);
        // when
        CategoryResponse response = categoryService.findAll();
        // then
        assertThat(response.getCategories()).containsExactly(categoryDto1,categoryDto2);
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldNotReturnAllCategoriesDto() {
        // given
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        CategoryResponse response = categoryService.findAll();
        // then
        assertThat(response.getCategories()).isEmpty();
        verify(categoryRepository).findAll();
    }

    @Test
    void shouldReturnCategoriesDtoByNameStartingWith(){
        // given
        Category category1 = new Category("Drinks");
        Category category2 = new Category("Desserts");
        List<Category> categories = Arrays.asList(category1,category2);
        CategoryDto categoryDto1 = new CategoryDto("Drinks");
        CategoryDto categoryDto2 = new CategoryDto("Desserts");
        when(categoryRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(categories);
        when(modelMapper.map(category1,CategoryDto.class)).thenReturn(categoryDto1);
        when(modelMapper.map(category2,CategoryDto.class)).thenReturn(categoryDto2);
        // when
        CategoryResponse response = categoryService.findCategoriesByName(Mockito.anyString());
        // then
        assertThat(response.getCategories()).containsExactly(categoryDto1,categoryDto2);
        verify(categoryRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldNotReturnCategoriesDtoByNameStartingWith() {
        // given
        when(categoryRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(Collections.emptyList());
        // when
        CategoryResponse response = categoryService.findCategoriesByName(Mockito.anyString());
        // then
        assertThat(response.getCategories()).isEmpty();
        verify(categoryRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldAddCategory(){
        // given
        CategoryDto categoryDto = new CategoryDto("Drinks");
        when(categoryRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        when(modelMapper.map(categoryDto,Category.class)).thenReturn(new Category(categoryDto.getName()));
        // then
        categoryService.addCategory(categoryDto);
        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category category = categoryCaptor.getValue();
        assertThat(category.getName()).isEqualTo(categoryDto.getName());
    }

    @Test
    void shouldNotAddCategory() {
        // given
        CategoryDto categoryDto = new CategoryDto("Drinks");
        Category existingCategory = new Category();
        when(categoryRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(existingCategory));
        when(modelMapper.map(categoryDto,Category.class)).thenReturn(new Category(categoryDto.getName()));
        // when & then
        assertThatThrownBy(() -> categoryService.addCategory(categoryDto))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessageContaining("Category has already been added with this name");
    }

    @Test
    void shouldDeleteCategoryById(){
        // given
        int id = 1;
        Category category = new Category();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(id);
        // when
        categoryService.deleteCategory(id);
        // then
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(categoryRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
        verify(categoryRepository).findById(id);
    }

    @Test
    void shouldUpdateCategoryById(){
        // given
        CategoryDto categoryDto = new CategoryDto("Drinks");
        Category existingCategory = new Category("Drink");
        when(categoryRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(existingCategory));
        when(modelMapper.map(categoryDto,Category.class)).thenReturn(new Category(categoryDto.getName()));
        // when
        categoryService.updateCategory(Mockito.anyInt(),categoryDto);
        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category category = categoryCaptor.getValue();
        assertThat(category.getName()).isEqualTo(categoryDto.getName());
        verify(categoryRepository).findById(Mockito.anyInt());
    }



}