package com.ecommerce.WatchStore.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BillDTO {
    private Long id;

    private Long userId;
    private String deliverAddress;
    private LocalDateTime createDate;
    private Long voucherId;
    private float totalPrice;
    private String createdBy;
    private LocalDateTime updateDate;
    private String updatedBy;
    private String status;
    private String currency;
    private String method;
    private String intent;
    private String description;
    private List<BillDetailDTO> billDetailDTOList;

}