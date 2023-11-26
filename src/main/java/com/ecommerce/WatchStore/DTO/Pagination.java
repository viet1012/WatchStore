package com.ecommerce.WatchStore.DTO;

import lombok.*;

import java.util.List;
@Getter
@Setter
public class Pagination<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;

    // Constructors, getters, and setters

    public Pagination(List<T> data, int currentPage, int pageSize, int totalPages) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
    }
    public Pagination(List<T> data, int currentPage, int pageSize, int totalPages, long totalItems) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }
    // Getters and setters
}

