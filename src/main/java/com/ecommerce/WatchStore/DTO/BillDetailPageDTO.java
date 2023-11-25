package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.BillDetail;

import java.util.List;

public class BillDetailPageDTO extends Pagination<BillDetail>{
    public BillDetailPageDTO(List<BillDetail> data, int currentPage, int pageSize, int totalPages) {
        super(data, currentPage, pageSize, totalPages);
    }
}
