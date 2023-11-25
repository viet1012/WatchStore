package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.DTO.CategoryDTO;
import com.ecommerce.WatchStore.DTO.CategoryPageDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Category;
import com.ecommerce.WatchStore.Entities.Supplier;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
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
    public ResponseEntity<ResponseWrapper<List<Category>>> getAllCategory() {
        List<Category> categoryList = categoryService.getAllCategory();
        long totalCategories = categoryService.getTotalCategories();
        ResponseWrapper<List<Category>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalCategories, categoryList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/Items")
    public ResponseEntity<ResponseWrapper<CategoryPageDTO>> getCategories(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<Category> categories = categoryService.getCategoriesByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalCategories = categoryService.getTotalCategories();
        int totalPages = (int) Math.ceil(totalCategories / (double) pageSize);

        CategoryPageDTO categoryPageDTO = new CategoryPageDTO(categories, page, pageSize, totalPages);

        ResponseWrapper<CategoryPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, categoryPageDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<Category>> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category savedCategory = new Category();
        savedCategory.setName(categoryDTO.getName());
        savedCategory.setCreatedBy(categoryDTO.getCreatedBy());
        savedCategory.setCreatedDt(categoryDTO.getCreatedDt());
        savedCategory.setActive(categoryDTO.isActive());

        Category newCategory = categoryService.saveCategory(savedCategory);

        ResponseWrapper<Category> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Category created successfully", true, newCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<ResponseWrapper<Category>> updateCategory(@PathVariable Long id, @RequestBody Category categoryDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
        if (updatedCategory != null) {
            ResponseWrapper<Category> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Category updated successfully", true, updatedCategory);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        ResponseWrapper<String> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Category with ID " + id + " deleted successfully", true, null);
        return ResponseEntity.ok().body(response);
    }

}

