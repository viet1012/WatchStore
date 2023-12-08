package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import org.hibernate.query.criteria.JpaParameterExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {

    @Query("SELECT  b.id AS billId, b.totalPrice AS total, b.user.id AS userId, b.deliverAddress AS deliverAddress ,bd AS billDetails, b.voucher.id " +
            "FROM Bill b INNER JOIN b.billDetailList bd")
    List<Object[]> getAllBillDetails();
    @Query("SELECT  b.id AS billId, b.totalPrice AS total, b.user.id AS userId, b.deliverAddress AS deliverAddress, bd AS billDetails, b.voucher.id " +
            "FROM Bill b INNER JOIN b.billDetailList bd WHERE b.user.id = :userId")
    List<Object[]> getAllBillDetailsByUserId(@Param("userId") Long userId);

}
