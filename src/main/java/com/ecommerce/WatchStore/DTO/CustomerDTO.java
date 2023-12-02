package com.ecommerce.WatchStore.DTO;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class CustomerDTO {
    private String email;
    private String password;
    private String displayName;
    private String fullname;
    private String code;
    private String lastName;
    private String firstName;
    private String gender;
    private LocalDate dateOfBirth;
    private String address;
    private String phoneNumber;
    private String createdBy;
    private Boolean active;


}
