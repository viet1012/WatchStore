package com.ecommerce.WatchStore.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.WatchStore.Entities.Receipt;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    @Query("SELECT  r.id AS receiptDetailId, r.total AS total, r.supplier.id AS supplierId, rd AS receiptDetails " +
            "FROM Receipt r INNER JOIN r.receiptDetails rd")
    List<Object[]> getAllReceiptDetailsWithTotalAndSupplierId();
}
