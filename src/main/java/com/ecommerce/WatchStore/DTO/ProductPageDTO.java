package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.Product;
import lombok.*;
import java.util.List;

@Getter
@Setter
//public class ProductPageDTO {
//    private List<Product> products;
//    private int currentPage;
//    private int pageSize;
//    private int totalPages;
//
//}
public class ProductPageDTO extends Pagination<Product> {
    public ProductPageDTO(List<Product> data, int currentPage, int pageSize, int totalPages) {
        super(data, currentPage, pageSize, totalPages);
    }
    // Các trường của ProductPageDTO
}