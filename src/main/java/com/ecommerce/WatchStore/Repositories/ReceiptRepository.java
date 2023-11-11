package com.ecommerce.WatchStore.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.WatchStore.Entities.Receipt;
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    // Các phương thức truy vấn cụ thể nếu cần
}
