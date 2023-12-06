package com.ecommerce.WatchStore.DTO;

import lombok.*;

@Getter
@Setter
public class AddressDTO {
    private String token;
    private Long userId;
    private String fullname;
    private String ward;
    private String district;
    private String houseNumber;
    private String note;

}

