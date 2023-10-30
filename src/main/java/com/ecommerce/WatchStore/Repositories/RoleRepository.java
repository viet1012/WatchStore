package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleTitle(String roleTitle);
}

