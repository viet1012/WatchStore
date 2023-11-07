package com.ecommerce.WatchStore.Services;


import com.ecommerce.WatchStore.Entities.Role;
import com.ecommerce.WatchStore.Entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoUserDetails implements UserDetails {
    private String email;
    private String password;
    private List<GrantedAuthority> authorities;


    public UserInfoUserDetails(User user, List<Role> roles) {
        email = user.getEmail();
        password = user.getPassword();
        authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleTitle()))
                .collect(Collectors.toList());
    }

    public UserInfoUserDetails(User user, Role role) {
        email = user.getEmail();
        password = user.getPassword();
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getRoleTitle()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Thay đổi thành true nếu tài khoản không bị hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Thay đổi thành true nếu tài khoản không bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Thay đổi thành true nếu thông tin xác thực không bị hết hạn
    }

    @Override
    public boolean isEnabled() {
        return true; // Thay đổi thành true nếu tài khoản được kích hoạt
    }
}
