package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Services.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/reset-password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePasswordReset(@RequestBody User userDTO) {

        // Gọi service để khởi tạo quá trình đặt lại mật khẩu

        return new ResponseEntity<>("Yêu cầu đặt lại mật khẩu đã được khởi tạo. Vui lòng kiểm tra email của bạn.", HttpStatus.OK);
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(String email) {

        return new ResponseEntity<>("Yêu cầu đặt lại mật khẩu đã được khởi tạo. Vui lòng kiểm tra email của bạn.", HttpStatus.OK);
    }
}
