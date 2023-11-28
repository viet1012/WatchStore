package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.Receipt;
import lombok.*;

@Getter
@Setter

public class ReceiptDetailDTO {
    private Long id;

    private long receiptId;

    private long productId;


    private int quantity;

    private double price;


}
