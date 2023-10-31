package com.ecommerce.WatchStore.Services;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import com.ecommerce.WatchStore.Entities.Permission;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.RolePermission;
import com.ecommerce.WatchStore.Repositories.PermissionRepository;
import com.ecommerce.WatchStore.Repositories.RolePermissionRepository;
import com.ecommerce.WatchStore.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService {

//    public boolean currentUserHasPermissionToAssignRolePermission() {
//        // Lấy thông tin xác thực của người dùng hiện tại
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        // Kiểm tra quyền của người dùng dựa trên thông tin xác thực
//        if (authentication != null && authentication.getAuthorities().stream()
//                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
//            // Người dùng có quyền ROLE_ADMIN, có thể gán quyền cho vai trò
//            return true;
//        } else {
//            // Người dùng không có quyền thực hiện thao tác này
//            return false;
//        }
//    }

    @Autowired
    private  RolePermissionRepository rolePermissionRepository;
    @Autowired
    private  RoleRepository roleRepository;
    @Autowired
    private  PermissionRepository permissionRepository;

    public List<RolePermission> getAllRolePermissions(){
        return rolePermissionRepository.findAll();
    }
    //Lấy RolePermission dựa trên ID
    public RolePermission getRolePermissionById(Long rolePermissionId) {
        return rolePermissionRepository.findById(rolePermissionId)
                .orElseThrow(() -> new RuntimeException("RolePermission không tồn tại"));
    }
    //Lấy danh sách RolePermissions của một Role cụ thể:
    public List<RolePermission> getRolePermissionsByRoleId(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
        return rolePermissionRepository.findByRole(role);
    }

    //Lấy danh sách RolePermissions của một Id cụ thể:
    public List<RolePermission> getRolePermissionsByPermissionId(Long permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));
        return rolePermissionRepository.findByPermission(permission);
    }

    //Xóa RolePermission dựa trên ID:
    public void deleteRolePermission(Long rolePermissionId) {
        rolePermissionRepository.deleteById(rolePermissionId);
    }

    public void removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));

        RolePermission rolePermission = rolePermissionRepository.findByRoleAndPermission(role, permission);
        if (rolePermission != null) {
            rolePermissionRepository.delete(rolePermission);
        }
    }


    public RolePermission assignPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Quyền không tồn tại"));

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        return rolePermissionRepository.save(rolePermission);
    }

}
