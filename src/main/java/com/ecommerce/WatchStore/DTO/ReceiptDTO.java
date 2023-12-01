package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReceiptDTO {
    private Long id;
    private double total;
    private long supplierId;
    private List<ReceiptDetailDTO> receiptDetails;

}
