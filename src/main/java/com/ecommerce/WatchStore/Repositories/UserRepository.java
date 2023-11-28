package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByDisplayName(String displayName);
    @Query("SELECT u.role FROM User u WHERE u.role.id = :roleId")
    Role findRoleByRoleId(@Param("roleId") Long roleId);
}

