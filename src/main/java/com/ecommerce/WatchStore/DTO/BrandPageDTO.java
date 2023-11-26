package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Brand;

import java.util.List;

public class BrandPageDTO extends Pagination<Brand>{
    public BrandPageDTO(List<Brand> data, int currentPage, int pageSize, int totalPages , long totalItems) {
        super(data, currentPage, pageSize, totalPages, totalItems);
    }
}
