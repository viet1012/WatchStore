package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.DTO.CategoryDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Category;
import com.ecommerce.WatchStore.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/GetAll")
    public ResponseEntity<List<Category>> getAllCategory() {
        List<Category> categoryList = categoryService.getAllCategory();
        return ResponseEntity.ok(categoryList);
    }

    @PostMapping("/Create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category savedCategory = new Category();
        savedCategory.setName(categoryDTO.getName());
        savedCategory.setCreatedBy(categoryDTO.getCreatedBy());
        savedCategory.setCreatedDt(categoryDTO.getCreatedDt());
        savedCategory.setActive(categoryDTO.isActive());

        Category newCategory = categoryService.saveCategory(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
        if (updatedCategory != null) {
            return ResponseEntity.ok(updatedCategory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body("Category with ID " + id + " deleted successfully");
    }
}

