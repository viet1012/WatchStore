package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Category;
import com.ecommerce.WatchStore.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }
    public Optional<Category> getCategoryById(Long id){
        return categoryRepository.findById(id);
    }
    public Category saveCategory(Category category){
        return categoryRepository.save(category);
    }
    public Category updateCategory(Category updatedCategory){
        Optional<Category> existingCategoryOptional = categoryRepository.findById(updatedCategory.getIdBrand());
        if(existingCategoryOptional.isPresent()){
            Category existingCategory = existingCategoryOptional.get();
            existingCategory.setName(updatedCategory.getName());
            return categoryRepository.save(existingCategory);
        }else
        {
            throw  new RuntimeException("Không tìm thấy danh mục với ID: " + updatedCategory.getIdBrand());
        }
    }
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
