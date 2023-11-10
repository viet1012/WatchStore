package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    // Các phương thức truy vấn cụ thể nếu cần
}
