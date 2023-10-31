package com.ecommerce.WatchStore.Repositories;

import com.ecommerce.WatchStore.Entities.Permission;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    RolePermission findByRoleAndPermission(Role role, Permission permission);
    List<RolePermission> findByRole(Role role);
    List<RolePermission> findByPermission(Permission permission);
}
