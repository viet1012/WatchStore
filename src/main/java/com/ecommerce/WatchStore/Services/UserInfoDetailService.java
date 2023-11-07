package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.RoleRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Repositories.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserInfoDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserInfoDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            System.out.println("User not found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Get the roles for the user
        Role userRoles = userRepository.findRoleByRoleId(optionalUser.get().getRole().getId());

        // Create UserInfoUserDetails with the found user and roles
        return new UserInfoUserDetails(optionalUser.get(), userRoles);
    }

}
