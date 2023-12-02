package com.ecommerce.WatchStore.DTO;

import com.ecommerce.WatchStore.Entities.User;
import lombok.*;

@Getter
@Setter
public class UserRegistrationRequest {
    private User user;
    private CustomerDTO customerDTO;

}
