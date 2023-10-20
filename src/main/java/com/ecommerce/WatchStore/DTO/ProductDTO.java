package com.ecommerce.WatchStore.DTO;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class ProductDTO {
    private Long productId;
    private String productName;
    private String img;
    private MultipartFile imageFile; // Tệp ảnh
    private float price;
    private Integer quantity;
    private String brandName; // Thêm trường này để lưu tên brand

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    // Các getter và setter
}

