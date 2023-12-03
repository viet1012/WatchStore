package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.ReviewProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewProductRepository extends JpaRepository<ReviewProduct, Long> {
    @Query("SELECT r FROM ReviewProduct r WHERE r.product.productId = :productId")
    List<ReviewProduct> findByProductId(@Param("productId") Long productId);
}