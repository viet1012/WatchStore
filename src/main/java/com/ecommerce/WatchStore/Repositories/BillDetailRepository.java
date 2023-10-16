package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDetailRepository  extends JpaRepository<BillDetail,Long> {
}
