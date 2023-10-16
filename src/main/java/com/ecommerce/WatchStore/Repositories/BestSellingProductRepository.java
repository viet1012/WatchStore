package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BestSellingProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT bd.product.productId, bd.product.productName, SUM(bd.quantity), bd.product.brand.idBrand, bd.product.brand.name " +
            "FROM BillDetail bd " +
            "GROUP BY bd.product.productId, bd.product.productName, bd.product.brand.idBrand, bd.product.brand.name " +
            "ORDER BY SUM(bd.quantity) DESC")
    List<Object[]> findBestSellingProducts();


}
