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

    public Category saveCategory(Category newCategory){
        Optional<Category> existingCategory = categoryRepository.findCategoryByName(newCategory.getName());
        if (existingCategory.isPresent()) {
            throw new RuntimeException("Tên danh mục đã tồn tại.");
        }
        else{
            return categoryRepository.save(newCategory);
        }
    }

    public Category updateCategory(Long id,Category updatedCategory){
        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);
        if(existingCategoryOptional.isPresent()){
            Category existingCategory = existingCategoryOptional.get();
            existingCategory.setName(updatedCategory.getName());
            existingCategory.setUpdatedDt(updatedCategory.getUpdatedDt());
            existingCategory.setUpdatedBy(updatedCategory.getUpdatedBy());

            return categoryRepository.save(existingCategory);
        }else
        {
            throw  new RuntimeException("Không tìm thấy danh mục với ID: " + updatedCategory.getIdCategory());
        }
    }
    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}
