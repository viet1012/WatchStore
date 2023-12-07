package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Brand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BestSellingProductDTO
{
    private Long productId;
    private String productName;
    private int quantity;
    private Long totalQuantitySold;
    private Long brandId;
    private  String brandName;
    private float price;

}
