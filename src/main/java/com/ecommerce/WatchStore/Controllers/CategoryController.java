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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategory(){
        List<Category> categoryList = categoryService.getAllCategory();
        return ResponseEntity.ok(categoryList);
    }
    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO categoryDTO){
        Category savedCategory = new Category();
        savedCategory.setName(categoryDTO.getName());
        savedCategory.setCreatedBy(categoryDTO.getCreatedBy());
        savedCategory.setCreatedDt(categoryDTO.getCreatedDt());
        savedCategory.setActive(categoryDTO.isActive());

        Category newCategory = categoryService.saveCategory(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id,@RequestBody Category categoryDTO){

        Category newCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
