package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillDetailRepository  extends JpaRepository<BillDetail,Long> {
    List<BillDetail> findByBill(Bill bill);
}
