package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import org.hibernate.query.criteria.JpaParameterExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill,Long> {

//    @Query("SELECT  b.id AS billId, b.totalPrice AS total, b.user.id AS userId, b.deliverAddress AS deliverAddress, bd AS billDetails, b.voucher.id, b.createdDate, b.status " +
//            "FROM Bill b inner JOIN b.billDetailList bd"  )
    @Query("SELECT b.id AS billId, b.totalPrice AS totalPrice, b.user.id AS userId, b.deliverAddress AS deliverAddress, b.voucher.id AS voucherId, b.createdDate as create_dt, b.status as status, " +
            " bd.product.productId AS productId, bd.unitPrice AS unitPrice, bd.quantity AS quantity " +
            "FROM Bill b JOIN b.billDetailList bd ")
    List<Object[]> getAllBillDetails();


    @Query("SELECT b.id AS billId, b.totalPrice AS totalPrice, b.user.id AS userId, b.deliverAddress AS deliverAddress, b.voucher.id AS voucherId, b.createdDate as create_dt, b.status as status, " +
            " bd.product.productId AS productId, bd.unitPrice AS unitPrice, bd.quantity AS quantity " +
            "FROM Bill b JOIN b.billDetailList bd " +  " WHERE b.user.id = :userId ")
    List<Object[]> getAllBillDetailsByUserId(@Param("userId") Long userId);

    @Query("SELECT b.id AS billId, b.totalPrice AS totalPrice, b.user.id AS userId, b.deliverAddress AS deliverAddress, b.voucher.id AS voucherId, " +
            "bd.id AS billDetailId, bd.product.productId AS productId, bd.unitPrice AS unitPrice, bd.quantity AS quantity " +
            "FROM Bill b JOIN b.billDetailList bd " +
            "WHERE b.id = :billId and b.user.id = :userId ")
    List<Object[]> getBillWithBillDetailsById(@Param("billId") Long billId, @Param("userId") Long userId );


    @Query("SELECT b FROM Bill b WHERE b.createdDate = (SELECT MIN(bill.createdDate) FROM Bill bill)")
    Optional<Bill> findBillWithEarliestCreationDate();

}
