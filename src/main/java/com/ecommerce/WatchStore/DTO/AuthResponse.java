package com.ecommerce.WatchStore.DTO;

import lombok.*;
import com.ecommerce.WatchStore.Entities.User;

@Setter
@Getter
public class AuthResponse {
    private User user;
    private String accessToken;
    private String token;
    public AuthResponse(){

    }


}
