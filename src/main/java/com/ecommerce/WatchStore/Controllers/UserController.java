package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private  UserService userService;
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
    public ResponseEntity<String> registerUserWithRole(@RequestBody User registrationDto, @RequestParam("role_id") Long roleId) {
        // Kiểm tra xem email đã được sử dụng chưa
        if (userService.isEmailTaken(registrationDto.getEmail())) {
            return new ResponseEntity<>("Email đã được sử dụng.", HttpStatus.BAD_REQUEST);
        }

        // Tạo một người dùng mới
        userService.registerUser(registrationDto.getEmail(), registrationDto.getPassword(), registrationDto.getDisplayName());

        // Gán vai trò cho người dùng
        userService.assignRoleToUser(registrationDto.getId(), roleId);

        return new ResponseEntity<>("Đăng ký thành công và đã gán vai trò.", HttpStatus.CREATED);
    }

}
