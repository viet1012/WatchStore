package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM product_models p ORDER BY  p.price ASC ")
    List<Product> findAllProductByPriceAsc();
//    @Query("SELECT p from Product p ORDER BY p.price DESC ")
//    List<Product> findAllProductByPriceDesc();
      boolean existsByProductName(String productName);

}
