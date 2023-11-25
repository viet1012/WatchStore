package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Config.JwtAuthenticationResponse;
import com.ecommerce.WatchStore.Config.JwtTokenProvider;
import com.ecommerce.WatchStore.DTO.AuthResponse;
import com.ecommerce.WatchStore.DTO.OTPRequest;
import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.EmailService;
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
@RequestMapping("/api/User")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<User>>> getListUser() {
        List<User> users = userService.getListUser();
        long totalUsers = userService.getTotalUsers();
        ResponseWrapper<List<User>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalUsers, users);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapper<User>> findUserById(@RequestParam Long id) {
        User user = userService.getUserById(id);
        ResponseWrapper<User> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<User>> createUser(@RequestBody UserDTO userDTO) {
        User newUser = userService.createUser(userDTO);
        ResponseWrapper<User> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "User created successfully", true, newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/Update")
    public ResponseEntity<ResponseWrapper<User>> updateUser(@RequestBody User user, @RequestParam Long userId) {
        User updatedUser = userService.updateUser(user, userId);
        ResponseWrapper<User> response = new ResponseWrapper<>(HttpStatus.ACCEPTED.value(), "User updated successfully", true, updatedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/Get-role/{id}")
    public ResponseEntity<ResponseWrapper<Role>> getRoleId(@PathVariable Long id) {
        Role role = userService.findRoleByRoleId(id);
        ResponseWrapper<Role> response = new ResponseWrapper<>(HttpStatus.ACCEPTED.value(), "Success", true, role);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

}
