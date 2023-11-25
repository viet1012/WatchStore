package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Receipt;

import java.util.List;

public class ReceiptPageDTO extends Pagination<Receipt>{
    public ReceiptPageDTO(List<Receipt> data, int currentPage, int pageSize, int totalPages) {
        super(data, currentPage, pageSize, totalPages);
    }
}
