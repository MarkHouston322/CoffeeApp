package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.CategoryConstants;
import CoffeeApp.storageservice.dto.categoryDto.CategoryDto;
import CoffeeApp.storageservice.dto.categoryDto.CategoryResponse;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
@AllArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public CategoryResponse getCategories(){
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Integer id){
        CategoryDto categoryDto = categoryService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDto);
    }

    @GetMapping("/get/{name}")
    public CategoryResponse getCategoriesByName(@PathVariable("name") String name){
        return categoryService.findCategoriesByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addCategory(@Valid @RequestBody CategoryDto categoryDto){
        categoryService.addCategory(categoryDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CategoryConstants.STATUS_201, CategoryConstants.MESSAGE_201));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseDto> updateCategory(@PathVariable("id") Integer id,
                                                      @Valid @RequestBody CategoryDto categoryDto){
        boolean isUpdated = categoryService.updateCategory(id, categoryDto);
        if (isUpdated){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CategoryConstants.STATUS_417, CategoryConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto> deleteCategory(@PathVariable("id") Integer id){
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted){
            return responseStatusOk();
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CategoryConstants.STATUS_417, CategoryConstants.MESSAGE_417_DELETE));
        }
    }

    private ResponseEntity<ResponseDto> responseStatusOk(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(CategoryConstants.STATUS_200, CategoryConstants.MESSAGE_200));
    }
}
