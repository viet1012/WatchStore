package com.ecommerce.WatchStore.Repositories;
import org.springframework.data.repository.query.Param;
import com.ecommerce.WatchStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM product_models p ORDER BY  p.price ASC ")
    List<Product> findAllProductByPriceAsc();
    @Query("SELECT p from product_models p ORDER BY p.price DESC ")
    List<Product> findAllProductByPriceDesc();
    boolean existsByProductName(String productName);
    List<Product> findByProductNameContaining(String keyword);
    @Query("select p from product_models p where  p.brand.idBrand = :brandId")
    List<Product> findProductsByBrandId(@Param("brandId") int brandId);
}
