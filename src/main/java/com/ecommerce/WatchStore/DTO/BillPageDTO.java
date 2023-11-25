package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Bill;

import java.util.List;

public class BillPageDTO extends Pagination<Bill>{
    public BillPageDTO(List<Bill> data, int currentPage, int pageSize, int totalPages) {
        super(data, currentPage, pageSize, totalPages);
    }
}
