package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface BestSellingProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT bd.product.productId, bd.product.productName, SUM(bd.quantity), bd.product.brand.idBrand, bd.product.brand.name " +
            "FROM BillDetail bd " +
            "GROUP BY bd.product.productId, bd.product.productName, bd.product.brand.idBrand, bd.product.brand.name " +
            "ORDER BY SUM(bd.quantity) DESC " +
            "LIMIT 1 ")
    List<Object[]> findBestSellingProducts();

    @Query("SELECT bd.product.productId, bd.product.productName, SUM(bd.quantity), bd.product.brand.idBrand, bd.product.brand.name " +
            "FROM BillDetail bd " +
            "GROUP BY bd.product.productId, bd.product.productName, bd.product.brand.idBrand, bd.product.brand.name " +
            "ORDER BY SUM(bd.quantity) DESC " +
            "LIMIT 3")
    List<Object[]> findTop3BestSellingProducts();

    @Query("SELECT product.productId, product.productName, product.quantity ,product.brand.idBrand, product.brand.name, product.price " +
            "FROM product_models  product " +
            "WHERE product.price >= :minPrice AND product.price <= :maxPrice " +
            "GROUP BY product.productId, product.productName, product.quantity , product.brand.idBrand, product.brand.name, product.price " +
            "ORDER BY product.price DESC")
    List<Object[]> findBestSellingProductsByPriceRange(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);


    @Query("SELECT bd.product.productId, bd.product.productName, SUM(bd.quantity), bd.product.brand.idBrand, bd.product.brand.name, bd.product.price " +
            "FROM BillDetail bd " +
            "WHERE bd.bill.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY bd.product.productId, bd.product.productName, bd.product.brand.idBrand, bd.product.brand.name, bd.product.price " +
            "ORDER BY SUM(bd.quantity) DESC")
    List<Object[]> findBestSellingProductsByTimeRange(@Param("startDate") LocalDateTime  startDate, @Param("endDate") LocalDateTime endDate);




}
