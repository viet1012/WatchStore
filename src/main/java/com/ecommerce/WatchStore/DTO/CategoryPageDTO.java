package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Category;

import java.util.List;

public class CategoryPageDTO extends Pagination<Category>{
    public CategoryPageDTO(List<Category> data, int currentPage, int pageSize, int totalPages) {
        super(data, currentPage, pageSize, totalPages);
    }
}
