package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Repositories.RoleRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Role createRole(Role role) {
        // Kiểm tra xem vai trò đã tồn tại chưa
        Role existingRole = roleRepository.findByRoleTitle(role.getRoleTitle());
        if (existingRole != null) {
            throw new RuntimeException("Vai trò đã tồn tại");
        }
        // Nếu không trùng lặp, thì mới lưu vào cơ sở dữ liệu
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long id, Role updatedRole) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role existingRole = roleOptional.get();
            // Cập nhật thông tin role với dữ liệu từ updatedRole
            existingRole.setRoleTitle(updatedRole.getRoleTitle());
            existingRole.setDescription(updatedRole.getDescription());
            existingRole.setUpdated_by(updatedRole.getUpdated_by());
            return roleRepository.save(existingRole);
        } else {
            // Xử lý khi không tìm thấy role
            throw new RuntimeException("Role with id " + id + " not found");
        }
    }

    @Transactional
    public void deleteRole(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            Role existingRole = roleOptional.get();
            roleRepository.delete(existingRole);
        } else {
            // Xử lý khi không tìm thấy role
            throw new RuntimeException("Role with id " + id + " not found");
        }
    }

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public Role getRoleByTitle(String roleName) {
        return roleRepository.findByRoleTitle(roleName);
    }

    public Role getRoleById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.orElse(null);
    }
}
