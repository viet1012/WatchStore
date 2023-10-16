package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Product;

import java.util.List;

public class ProductPageDTO {
    private List<Product> products;
    private int currentPage;
    private int pageSize;
    private int totalPages;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
