package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  BrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findBrandByName(String brandName);
    List<Brand> findAllByActiveTrue();
}
