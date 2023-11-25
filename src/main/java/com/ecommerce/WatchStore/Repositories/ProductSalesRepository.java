package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.DTO.ProductSalesDTO;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ProductSalesRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p.productId, p.productName, SUM(bd.quantity) AS totalQuantitySold " +
            "FROM product_models p " +
            "LEFT JOIN BillDetail bd ON p.productId = bd.product.productId " +
            "GROUP BY p.productId, p.productName")
    List<Object[]> getProductSales();
    
    @Query("SELECT bd.product.productId, SUM(bd.quantity) AS totalQuantitySold, bd.bill.totalPrice AS totalBillPrice " +
            "FROM BillDetail bd " +
            "WHERE bd.bill.createdDate >= :startDate " +
            "AND bd.bill.createdDate <= :endDate " +
            "GROUP BY bd.product.productId, bd.bill.totalPrice " +
            "ORDER BY totalBillPrice DESC, totalQuantitySold DESC")
    List<Object[]> findBestSellingProductByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
