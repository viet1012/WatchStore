package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
    List<ReceiptDetail> findAllByReceiptId(long receiptId);
}
