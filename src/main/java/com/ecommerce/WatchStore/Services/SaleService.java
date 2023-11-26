package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.DTO.ProductSalesDTO;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.ProductSalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private ProductSalesRepository productSalesRepository;

    public long getTotalProductSales()
    {
        return productSalesRepository.count();
    }
    public List<ProductSalesDTO> getProductSales() {

        List<Object[]> results = productSalesRepository.getProductSales();
        // Chuyển đổi kết quả truy vấn thành danh sách DTO
        List<ProductSalesDTO> productSales = new ArrayList<>();
        for (Object[] result : results) {
            ProductSalesDTO dto = new ProductSalesDTO();
            dto.setProductId((Long) result[0]);
            dto.setProductName((String) result[1]);
            dto.setTotalQuantitySold((Long) result[2]);

            productSales.add(dto);
        }
        return productSales;
    }
    public ProductSalesDTO getBestSellingProductByDateRange(LocalDateTime startDate, LocalDateTime endDate)
    {
        List<Object[]> result = productSalesRepository.findBestSellingProductByDateRange(startDate,endDate);
        if( result != null){
            Object[] bestSellingProductByDate = result.get(0);
            Long productId = (Long) bestSellingProductByDate[0];
            Long totalProduct = (Long) bestSellingProductByDate[1];
            float totalPrice = (float) bestSellingProductByDate[2];

            ProductSalesDTO productSalesDTO = new ProductSalesDTO();
            productSalesDTO.setProductId(productId);
            productSalesDTO.setTotalQuantitySold(totalProduct);
            productSalesDTO.setTotalPrice(totalPrice);
            return productSalesDTO;
        }
        return null;
    }

//    public ProductSalesDTO getUserWithMostPurchases()
//    {
//        List<Object[]> result = productSalesRepository.findUserWithMostPurchases();
//        if( result != null){
//            Object[] bestSeller = result.get(0);
//            Long userId = (Long) bestSeller[0];
//            Long totalProduct = (Long) bestSeller[1];
//
//            ProductSalesDTO productSalesDTO = new ProductSalesDTO();
//            productSalesDTO.setUserId(userId);
//            productSalesDTO.setTotalQuantitySold(totalProduct);
//            return productSalesDTO;
//        }
//        return null;
//    }

}
