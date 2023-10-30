package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Permission;
import com.ecommerce.WatchStore.Repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Transactional
    public Permission createPermission(Permission permission) {
        // Logic kiểm tra, gán giá trị và lưu mới Permission
        return permissionRepository.save(permission);
    }

    @Transactional
    public Permission updatePermission(Long id, Permission updatedPermission) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            Permission existingPermission = permissionOptional.get();
            // Cập nhật thông tin permission với dữ liệu từ updatedPermission
            existingPermission.setPermissionTitle(updatedPermission.getPermissionTitle());
            existingPermission.setDescription(updatedPermission.getDescription());
            // Các cập nhật khác tùy theo nhu cầu
            return permissionRepository.save(existingPermission);
        } else {
            // Xử lý khi không tìm thấy permission
            throw new RuntimeException("Permission with id " + id + " not found");
        }
    }

    @Transactional
    public void deletePermission(Long id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            Permission existingPermission = permissionOptional.get();
            permissionRepository.delete(existingPermission);
        } else {
            // Xử lý khi không tìm thấy permission
            throw new RuntimeException("Permission with id " + id + " not found");
        }
    }

    public List<Permission> getAllPermissions() {
        return (List<Permission>) permissionRepository.findAll();
    }

    public Permission getPermissionById(Long id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        return permissionOptional.orElse(null);
    }
}
