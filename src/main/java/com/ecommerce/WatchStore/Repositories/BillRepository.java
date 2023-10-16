package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import org.hibernate.query.criteria.JpaParameterExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {


}
