package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Entities.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    List<UserRoles> findByUserId(Long userId);
    void deleteByUserAndRole(User user, Role role);
    void deleteByUser(User user);
}