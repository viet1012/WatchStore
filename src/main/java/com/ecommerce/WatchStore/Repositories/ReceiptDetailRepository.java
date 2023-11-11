package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
    // Các phương thức truy vấn cụ thể nếu cần
}
