package com.ecommerce.WatchStore.Config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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
        // Tạo secretKey từ jwtSecret được cung cấp từ properties
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }



    public boolean verifyToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // Token hết hạn
            System.out.println("Token hết hạn: " + ex.getMessage());
            return false;
        } catch (UnsupportedJwtException ex) {
            // Token không được hỗ trợ
            System.out.println("Token không được hỗ trợ: " + ex.getMessage());
            return false;
        } catch (MalformedJwtException ex) {
            // Token không đúng định dạng
            System.out.println("Token không đúng định dạng: " + ex.getMessage());
            return false;
        } catch (SignatureException ex) {
            // Lỗi chữ ký không hợp lệ
            System.out.println("Lỗi chữ ký không hợp lệ: " + ex.getMessage());
            return false;
        } catch (IllegalArgumentException ex) {
            // Token rỗng hoặc không hợp lệ
            System.out.println("Token rỗng hoặc không hợp lệ: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            // Các lỗi khác
            System.out.println("Lỗi xác minh token: " + ex.getMessage());
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
        System.out.println("USER ID WHEN GET CLAIM : " + userId);
        if (userId != null) {
            return userId.longValue();
        }
        return null;
    }


//    public void handleSuccessfulLogin(Authentication authentication, long id) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Long userId = getUserIdFromToken(generateTokenWithUserId(userDetails, id)); // Thay YOUR_USER_ID bằng giá trị thực của userId
//
//
//    }

    public Long getUserIdFromGeneratedToken(String generatedToken) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(generatedToken).getBody();
            Number userId = (Number) claims.get("userId");
            if (userId != null) {
                return userId.longValue();
            }
        } catch (Exception ex) {
            System.out.println("Lỗi xác minh hoặc trích xuất userId từ token: " + ex.getMessage());
        }
        return null;
    }

    public String generateToken(Authentication authentication, long userId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String handleSuccessfulLogin(Authentication authentication, long userId) {
        return generateToken(authentication, userId);
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
        Long userId = claims.get("userId", Long.class); // Trích xuất userId từ claims


        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(username);
        userDetails.setUserId(userId);

        System.out.println("User Name: " + username);
        System.out.println("User ID: " + userId);
        // userDetails.setPassword(password);
        // Nếu có thông tin khác cần set

        return userDetails;
    }

}