package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Entities.UserRoles;
import com.ecommerce.WatchStore.Repositories.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserRoleService {

    @Autowired
    private UserRolesRepository userRoleRepository;

    public UserRoles addRoleToUser(User user, Role role) {
        UserRoles userRole = new UserRoles();
        userRole.setUser(user);
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }

    public void removeRoleFromUser(User user, Role role) {
        userRoleRepository.deleteByUserAndRole(user, role);
    }

    public void setRolesForUser(User user, Iterable<Role> roles) {
        // Xóa các vai trò hiện tại của người dùng
        userRoleRepository.deleteByUser(user);
        // Thêm các vai trò mới
        for (Role role : roles) {
            UserRoles userRole = new UserRoles();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }
    }
}
