package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
