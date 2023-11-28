package com.ecommerce.WatchStore.Config;

import com.ecommerce.WatchStore.Entities.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${app.jwtSecret}") String jwtSecret) {
        // Generate a secure key using Keys.secretKeyFor
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }


    public boolean verifyToken(String token) {
        try {
            // Parse token và lấy ra thông tin claims
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // Token đã hết hạn
            return false;
        } catch (Exception ex) {
            // Token không hợp lệ hoặc không thể xác minh
            return false;
        }
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateTokenWithUserId(UserDetails userDetails, long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Thêm userId vào claims

        return createToken(claims, userDetails.getUsername());
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        Number userId = (Number) claims.get("userId");
        if (userId != null) {
            return userId.longValue();
        }
        return null; // Hoặc giá trị mặc định khác tùy thuộc vào logic của bạn
    }


    public void handleSuccessfulLogin(Authentication authentication , long id) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = getUserIdFromToken(generateTokenWithUserId(userDetails, id)); // Thay YOUR_USER_ID bằng giá trị thực của userId

        // Lưu thông tin địa chỉ của người dùng sau khi đăng nhập thành công
    }


    public String generateToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        if (userDetails != null) {

            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        return null;
    }

    private CustomUserDetails getUserDetailsFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        // Lấy thông tin khác từ token nếu cần
        // Ví dụ: String password = ...;

        System.out.println("User Name: " + username);
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(username);
        // userDetails.setPassword(password);
        // Nếu có thông tin khác cần set

        return userDetails;
    }

}