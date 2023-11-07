package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Config.JwtAuthenticationResponse;
import com.ecommerce.WatchStore.Config.JwtTokenProvider;
import com.ecommerce.WatchStore.DTO.AuthResponse;
import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private  UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;
    @GetMapping("/list")
    public ResponseEntity<List<User>> getListUser() {
        List<User> users = userService.getListUser();
        return ResponseEntity.ok(users);
    }
    @GetMapping()
    public ResponseEntity<User> findUserById(@RequestParam Long id){
        User user= userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
            User newUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestParam Long userId) {
        User updatedUser = userService.updateUser(user, userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUserWithRole(@RequestBody User registrationDto, @RequestParam("role_id") Long roleId) {
        // Kiểm tra xem email đã được sử dụng chưa

        // Tạo một người dùng mới
        User registeredUser = userService.registerUser(registrationDto, roleId);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setUser(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

        //return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping("/get-role/{id}")
    public ResponseEntity<Role> getRoleId(@PathVariable Long id){
        Role role =  userService.findRoleByRoleId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(role);
    }

}
