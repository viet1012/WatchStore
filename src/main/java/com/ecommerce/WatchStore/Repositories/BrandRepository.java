package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  BrandRepository extends JpaRepository<Brand,Integer> {
    Optional<Brand> findBrandByName(String brandName);
}
