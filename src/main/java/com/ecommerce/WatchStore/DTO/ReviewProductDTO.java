package com.ecommerce.WatchStore.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewProductDTO {
    private String token;
    private Long id;
    private Long userId;
    private Long productId;
    private int rating;
    private String comment;
    private LocalDateTime createdDt;


}
