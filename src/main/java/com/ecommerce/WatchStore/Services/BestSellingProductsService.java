package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.Repositories.BestSellingProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
