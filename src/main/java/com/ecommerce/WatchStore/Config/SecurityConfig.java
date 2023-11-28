package com.ecommerce.WatchStore.Config;

import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Services.UserInfoDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    private UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoDetailService(repository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .authorizeRequests()
//                    .requestMatchers("/auth/login", "/auth/register", "/api/users/get-role/{id}").permitAll() // Cho phép mọi người truy cập /auth/login và /auth/register
//                    .requestMatchers("/user/**").hasRole("USER") // Đòi hỏi vai trò USER để truy cập các endpoint bắt đầu bằng /user/
//                    .requestMatchers("/admin/**").hasRole("ADMIN") // Đòi hỏi vai trò ADMIN để truy cập các endpoint bắt đầu bằng /admin/
//                    .anyRequest().authenticated() // Tất cả các request còn lại yêu cầu xác thực
//                    .and()
//                    .formLogin(); // Để sử dụng trang đăng nhập mặc định của Spring Security nếu không được xác thực
//            return http.build();
//        }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/**").permitAll() // Cho phép mọi người truy cập tất cả các endpoint mà không cần xác thực
                .anyRequest().authenticated() // Các yêu cầu còn lại yêu cầu xác thực
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .formLogin(); // Để sử dụng trang đăng nhập mặc định của Spring Security nếu không được xác thực
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
