package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Services.EmailService;
import com.ecommerce.WatchStore.Utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/Admin")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send-email-ads")
    public ResponseEntity<String> sendEmailADS(
            @RequestParam("recipientEmail") String recipientEmail
    ) {
        try {
            emailService.sendEmailWithAds(recipientEmail);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }

    @PostMapping("/send-email-otp")
    public ResponseEntity<String> sendEmailOTP(
       @RequestBody User user
    ) {
        try {
            OtpUtils otpUtils = new OtpUtils();
            String otp = otpUtils.generateOtp();
            emailService.sendEmailWithOTP(user.getEmail(), user.getDisplayName(), otp);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }

    @PostMapping("/send-email-reset-password")
    public ResponseEntity<String> sendEmailResetPassword(
            @RequestBody User user
    ) {
        try {

            Optional<User> currentUser = userRepository.findByEmail(user.getEmail());
            if (currentUser.isPresent()) {
                OtpUtils otpUtils = new OtpUtils();
                String otp = otpUtils.generateOtp();
                emailService.sendVerificationEmail(currentUser.get().getEmail(), otp);

                currentUser.get().setOtp(otp);
                currentUser.get().setCreateDateOtp(LocalDateTime.now());
                userRepository.save(currentUser.get());

                return ResponseEntity.ok("Email sent to " + currentUser.get().getEmail() + " successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");

            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email");
        }
    }

}
