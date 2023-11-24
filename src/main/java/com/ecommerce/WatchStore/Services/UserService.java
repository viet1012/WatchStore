package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Config.JwtTokenProvider;
import com.ecommerce.WatchStore.DTO.UserDTO;
import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Entities.UserRoles;
import com.ecommerce.WatchStore.Repositories.RoleRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Repositories.UserRolesRepository;
import com.ecommerce.WatchStore.Utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    public List<User> getListUser() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
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

    public User updateUser(User user, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User updatedUser = userOptional.get();
            updatedUser.setDisplayName(user.getDisplayName());
            updatedUser.setUpdatedDate(user.getUpdatedDate());
            updatedUser.setPhoneNumber(user.getPhoneNumber());
            updatedUser.setUpdatedBy(user.getDisplayName());
            return userRepository.save(updatedUser);

        } else {
            throw new RuntimeException("Không tim thấy user phù hop");
        }
    }


    public User registerUser(User user, Long roleId) {
        Optional<User> isUserExist = userRepository.findByEmail(user.getEmail());
        User newUser = new User();

        if (isUserExist.isPresent()) {
            System.out.println("Email: " + user.getEmail() + " đã tồn tại");
        } else {
            Optional<Role> userRole = roleRepository.findById(roleId);
            newUser.setEmail(user.getEmail());
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            newUser.setPassword(encodedPassword);
            newUser.setDisplayName(user.getDisplayName());
            newUser.setCreatedBy(user.getDisplayName());
            newUser.setRole(userRole.get());
            OtpUtils otpUtils = new OtpUtils();
            String otp = otpUtils.generateOtp();
            LocalDateTime currentDateTime = LocalDateTime.now();
            newUser.setOtp(otp);
            newUser.setCreateDateOtp(currentDateTime);

            // sending otp to your email
            emailService.sendEmailWithOTP(newUser.getEmail(), newUser.getDisplayName(), newUser.getOtp());
        }
        System.out.println("Email: "+ newUser.getEmail() + "OTP: "+ newUser.getOtp());
        return userRepository.save(newUser);

    }

    public boolean reGenerateOtp(String email) {
        boolean isReGenerate = false;
        Optional<User> userCheck = userRepository.findByEmail(email);
        User user = userCheck.get();
        if (userCheck.isPresent()) {

            OtpUtils otpUtils = new OtpUtils();
            String otp = otpUtils.generateOtp();
            LocalDateTime currentDateTime = LocalDateTime.now();
            System.out.println("Thời gian hiện tại là: " + currentDateTime);

            user.setOtp(otp);
            user.setCreateDateOtp(currentDateTime);
            userRepository.save(user);
            emailService.sendEmailWithOTP(user.getEmail(), user.getDisplayName(), otp);
            isReGenerate = true;
        }

        return isReGenerate;
    }

    public boolean resetPassword(User user, String newPassword) {
        // Xác minh OTP trước khi đặt lại mật khẩu
        boolean isVerifyOTP = false;
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            isVerifyOTP = verifyOtp(existingUser.getEmail(), existingUser.getOtp());
            System.out.println("User: " + existingUser.getEmail() + " OTP: " + existingUser.getOtp() + "new Password "+ newPassword);

            if (isVerifyOTP) {
                try {

                    String encodedPassword = passwordEncoder.encode(newPassword);

                    existingUser.setPassword(encodedPassword);

                    userRepository.save(existingUser);
                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } else {
            isVerifyOTP = false;
        }
        return isVerifyOTP;
    }

    public boolean verifyOtp(String email, String otp) {
        boolean isVerify = false;
        Optional<User> userCheck = userRepository.findByEmail(email);
        User user = userCheck.get();
        if (userCheck != null) {

            LocalDateTime currentDateTime = LocalDateTime.now();
            // Tính thời gian chênh lệch
            Duration duration = Duration.between(user.getCreateDateOtp(), currentDateTime);
            long minutesDiff = duration.toMinutes();
            System.out.println("CreateDateOtp: " + user.getCreateDateOtp() + " currentDateTime: " + currentDateTime);

            System.out.println("Kiểm tra user: " + user.getOtp() + " minus: " + minutesDiff);

            if (minutesDiff <= 10 && user.getOtp().equals(otp)) {
                isVerify = true;
            } else {
                System.out.println("OTP: "+ otp +" đã hết hạn!");
            }
        }

        return isVerify;
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

    public Role findRoleByRoleId(Long id) {
        return userRepository.findRoleByRoleId(id);
    }
}
