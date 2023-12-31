package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer , Long> {
    Customer findByUserId(long id);
}
