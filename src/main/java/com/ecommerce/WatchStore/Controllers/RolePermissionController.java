package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Entities.RolePermission;
import com.ecommerce.WatchStore.Services.RolePermissionService;
import com.ecommerce.WatchStore.Entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {
    @Autowired
    private  RolePermissionService rolePermissionService;

    @PostMapping("/assign-role-permission")
    public ResponseEntity<String> assignRolePermission(
            @RequestParam Long roleId,
            @RequestParam Long permissionId
    ) {
        // 1. Xác minh quyền thực hiện thao tác (có thể sử dụng Spring Security)
//        if (!currentUserHasPermissionToAssignRolePermission()) {
//            return new ResponseEntity<>("Bạn không có quyền thực hiện thao tác này.", HttpStatus.FORBIDDEN);
//        }
        rolePermissionService.assignPermissionToRole(roleId, permissionId);
        return new ResponseEntity<>("Gán quyền thành công.", HttpStatus.OK);
    }
    @GetMapping
    public List<RolePermission> getAllRolePermissions() {
        return rolePermissionService.getAllRolePermissions();
    }

    @GetMapping("/{rolePermissionId}")
    public ResponseEntity<RolePermission> getRolePermissionById(@PathVariable Long rolePermissionId) {
        RolePermission rolePermission = rolePermissionService.getRolePermissionById(rolePermissionId);
        return ResponseEntity.ok(rolePermission);
    }

    @GetMapping("/role/{roleId}")
    public List<RolePermission> getRolePermissionsByRoleId(@PathVariable Long roleId) {
        return rolePermissionService.getRolePermissionsByRoleId(roleId);
    }

    @GetMapping("/permission/{permissionId}")
    public List<RolePermission> getRolePermissionsByPermissionId(@PathVariable Long permissionId) {
        return rolePermissionService.getRolePermissionsByPermissionId(permissionId);
    }

    @DeleteMapping("/{rolePermissionId}")
    public ResponseEntity<String> deleteRolePermission(@PathVariable Long rolePermissionId) {
        rolePermissionService.deleteRolePermission(rolePermissionId);
        return new ResponseEntity<>("RolePermission đã bị xóa thành công.", HttpStatus.OK);
    }


    @PostMapping("/remove")
    public ResponseEntity<String> removePermissionFromRole(@RequestParam Long roleId, @RequestParam Long permissionId) {
        rolePermissionService.removePermissionFromRole(roleId, permissionId);
        return new ResponseEntity<>("Loại bỏ quyền thành công.", HttpStatus.OK);
    }
}

