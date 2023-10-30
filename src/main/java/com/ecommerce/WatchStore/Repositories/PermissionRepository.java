package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Permission;
import com.ecommerce.WatchStore.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByPermissionTitle(String permissionTitle);
}
