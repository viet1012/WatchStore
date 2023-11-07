package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Entities.UserRoles;
import com.ecommerce.WatchStore.Repositories.RoleRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Repositories.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public List<User> getListUser(){
        return userRepository.findAll();
    }
    public User getUserById(Long userId){
        Optional<User> optionalUser =  userRepository.findById(userId);
        return optionalUser.orElse(null);
    }
    public User createUser(UserDTO userDTO) {

        User user = new User();

        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDisplayName(userDTO.getDisplayName());
        user.setCreatedBy(userDTO.getCreatedBy());
        user.setCreatedDate(userDTO.getCreatedDate());
        user.setActive(true);
        return userRepository.save(user);
    }
    public User updateUser(User user,Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User updatedUser = userOptional.get();
            updatedUser.setDisplayName(user.getDisplayName());
            updatedUser.setUpdatedDate(user.getUpdatedDate());
            updatedUser.setPhoneNumber(user.getPhoneNumber());

            return userRepository.save(updatedUser);

        }else {
            throw new RuntimeException("Không tim thấy user phù hop");
        }
    }
    public User registerUser(User user, Long roleId) {
        Optional<Role> userRole = roleRepository.findById(roleId);
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setDisplayName(user.getDisplayName());
        newUser.setRole(userRole.get());
        return userRepository.save(newUser);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId).orElse(null);
        Role role = roleRepository.findById(roleId).orElse(null);

        if (user != null && role != null) {
            UserRoles userRoles = new UserRoles();
            userRoles.setUser(user);
            userRoles.setRole(role);
            userRolesRepository.save(userRoles);
        }
    }

    public Role findRoleByRoleId(Long id){
        return userRepository.findRoleByRoleId(id);
    }
}
