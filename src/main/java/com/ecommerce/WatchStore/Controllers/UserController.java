package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Config.JwtAuthenticationResponse;
import com.ecommerce.WatchStore.Config.JwtTokenProvider;
import com.ecommerce.WatchStore.DTO.AuthResponse;
import com.ecommerce.WatchStore.DTO.OTPRequest;
import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
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
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private  UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

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

        // Tạo một người dùng mới
        User registeredUser = userService.registerUser(registrationDto, roleId);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setUser(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody User user)
    {

        boolean checkOTP = userService.resetPassword(user,  user.getNewPassword());
        if (checkOTP) {
            return "Tài khoản: "+ user.getEmail() +" đã đổi mật khẩu thành công";
        } else {
            return "OTP đã sai";
        }
    }

    @PostMapping("/verify-otp-registration")
    public String validateOTP(@RequestBody OTPRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();

        boolean check = userService.verifyOtp(email, otp);
        if (check) {
            emailService.sendEmailWithAds(otpRequest.getEmail());
            return "Tạo tài khoản thành công";
        } else {
            return "OTP đã quá 10 phút";
        }
    }

    @PostMapping("/re-generate-otp")
    public String regenerateOTP(@RequestBody OTPRequest email)
    {
        boolean check = userService.reGenerateOtp(email.getEmail());
        if(check)
            return "Đã gửi OTP thành công";
        else
            return "Gửi OTP thất bại";
    }

    @GetMapping("/get-role/{id}")
    public ResponseEntity<Role> getRoleId(@PathVariable Long id){
        Role role =  userService.findRoleByRoleId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(role);
    }

}
