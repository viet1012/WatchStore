package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.Repositories.BestSellingProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BestSellingProductsService {

    @Autowired
    private BestSellingProductRepository bestSellingProductRepository;
    public List<BestSellingProductDTO> getBestSellingProducts() {
        // Thực hiện truy vấn để thống kê sản phẩm được bán chạy nhất
        List<Object[]> results = bestSellingProductRepository.findBestSellingProducts();

        // Chuyển đổi kết quả truy vấn thành danh sách DTO
        List<BestSellingProductDTO> bestSellingProducts = new ArrayList<>();
        for (Object[] result : results) {
            BestSellingProductDTO dto = new BestSellingProductDTO();
            dto.setProductId((Long) result[0]);
            dto.setProductName((String) result[1]);
            dto.setTotalQuantitySold((Long) result[2]);
            dto.setBrandId((int) result[3]);
            dto.setBrandName((String) result[4]);
            bestSellingProducts.add(dto);
        }

        return bestSellingProducts;
    }

    public  List<BestSellingProductDTO> getTop3SellingProuducts(){
        List<Object[]> rs = bestSellingProductRepository.findTop3BestSellingProducts();
        List<BestSellingProductDTO> top3Products = new ArrayList<>();
        for (Object[] result : rs){
            BestSellingProductDTO dto = new BestSellingProductDTO();
            dto.setProductId((Long) result[0]);
            dto.setProductName((String) result[1]);
            dto.setTotalQuantitySold((Long) result[2]);
            dto.setBrandId((int) result[3]);
            dto.setBrandName((String) result[4]);
            top3Products.add(dto);
        }
        return top3Products;
    }

    public List<BestSellingProductDTO> getBestSellingProductsByPriceRange(double minPrice, double maxPrice) {
        List<Object[]> results = bestSellingProductRepository.findBestSellingProductsByPriceRange(minPrice, maxPrice);

        List<BestSellingProductDTO> bestSellingProducts = new ArrayList<>();
        for (Object[] result : results) {
            BestSellingProductDTO dto = new BestSellingProductDTO();
            dto.setProductId((Long) result[0]);
            dto.setProductName((String) result[1]);
            dto.setQuantity((int) result[2]);
            dto.setBrandId((int) result[3]);
            dto.setBrandName((String) result[4]);
            dto.setPrice((float) result[5]);
            bestSellingProducts.add(dto);
        }
        return bestSellingProducts;
    }


    public List<BestSellingProductDTO> getBestSellingProductsByTimeRange(LocalDateTime startDate, LocalDateTime  endDate) {
        List<Object[]> results = bestSellingProductRepository.findBestSellingProductsByTimeRange(startDate, endDate);

        List<BestSellingProductDTO> bestSellingProducts = new ArrayList<>();
        for (Object[] result : results) {
            BestSellingProductDTO dto = new BestSellingProductDTO();
            dto.setProductId((Long) result[0]);
            dto.setProductName((String) result[1]);
            dto.setTotalQuantitySold((Long) result[2]);
            dto.setBrandId((int) result[3]);
            dto.setBrandName((String) result[4]);
            bestSellingProducts.add(dto);
        }
        return bestSellingProducts;
    }

}
