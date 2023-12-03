package com.ecommerce.WatchStore.Controllers;


import com.ecommerce.WatchStore.Config.JwtAuthenticationResponse;
import com.ecommerce.WatchStore.Config.JwtTokenProvider;
import com.ecommerce.WatchStore.DTO.*;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Services.EmailService;
import com.ecommerce.WatchStore.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO loginRequest) {
        logger.info(userService.getUserIdFromEmail(loginRequest.getEmail()) + "----" +loginRequest.getEmail() + "---" + loginRequest.getPassword());
       User userLogin = userService.getUserFromEmail(loginRequest.getEmail());
       if(!userLogin.getActive())
       {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Đăng nhập không thành công ");
       }
       else {
           try {
               Authentication authentication = authenticationProvider.authenticate(
                       new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
               );
               UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
               String token = jwtTokenProvider.generateToken(authentication ,  userService.getUserIdFromEmail(loginRequest.getEmail()));

               return ResponseEntity.ok(new JwtAuthenticationResponse(token));
           } catch (AuthenticationException e) {
               e.printStackTrace();
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Đăng nhập không thành công " + e.getMessage());
           }
       }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUserWithRole( @RequestParam(name = "role_id", defaultValue = "2") Long roleId , @RequestBody CustomerDTO request) {


        System.out.println("User: " + request.getEmail());
        System.out.println("Fullname: " + request.getFullname());
        // Tạo một người dùng mới
        User registeredUser = userService.registerUser( roleId ,request);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getEmail());
        String token = jwtTokenProvider.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setUser(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);

    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody User user) {

        boolean checkOTP = userService.resetPassword(user, user.getNewPassword());
        if (checkOTP) {
            return "Tài khoản: " + user.getEmail() + " đã đổi mật khẩu thành công";
        } else {
            return "OTP đã sai";
        }
    }

    @PostMapping("/verify-otp-registration")
    public String validateOTP(@RequestBody OTPRequest otpRequest) {
        String email = otpRequest.getEmail();
        String otp = otpRequest.getOtp();
        User user = userService.getUserFromEmail(email);
        boolean check = userService.verifyOtp(email, otp);
        if (check) {
            emailService.sendEmailWithAds(otpRequest.getEmail());
            System.out.println("Hello: " + user.getEmail() + "Id: " +user.getId() );
            user.setActive(true);
            userService.savedUser(user);
            System.out.println("Active: " + user.getActive() );
            return "Tạo tài khoản thành công";
        } else {
            return "OTP đã quá 10 phút";
        }
    }

    @PostMapping("/re-generate-otp")
    public String regenerateOTP(@RequestBody OTPRequest email) {
        boolean check = userService.reGenerateOtp(email.getEmail());
        if (check)
            return "Đã gửi OTP thành công";
        else
            return "Gửi OTP thất bại";
    }


}
