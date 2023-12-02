package com.ecommerce.WatchStore.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String phoneNumber;
    private String displayName;
  //  private LocalDateTime lastOnline;
    private Long roleId;
    private Long customerId;
    private String createdBy;
    private LocalDateTime createdDate;
    //private String updatedBy;
    //private LocalDateTime updatedDate;
    private Boolean active;

}
