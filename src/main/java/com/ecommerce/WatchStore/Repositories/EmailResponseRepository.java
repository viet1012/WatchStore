package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.EmailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailResponseRepository extends JpaRepository<EmailResponse, Long> {
}
