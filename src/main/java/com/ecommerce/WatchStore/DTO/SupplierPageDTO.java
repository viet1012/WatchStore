package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Supplier;

import java.util.List;

public class SupplierPageDTO extends Pagination<Supplier>{
    public SupplierPageDTO(List<Supplier> data, int currentPage, int pageSize, int totalPages) {
        super(data, currentPage, pageSize, totalPages);
    }
}
