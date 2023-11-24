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
    private int brandId;
    private  String brandName;

    private float price;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(Long totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
