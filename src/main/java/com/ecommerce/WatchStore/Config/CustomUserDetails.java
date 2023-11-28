package com.ecommerce.WatchStore.Config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;


    // Constructor
    public CustomUserDetails(String username) {
        this.username = username;
    }
    public CustomUserDetails() {

    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Trả về danh sách quyền nếu có
    }

    @Override
    public String getPassword() {
        return null; // Trả về mật khẩu nếu cần thiết
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    // Các phương thức khác của UserDetails
}
