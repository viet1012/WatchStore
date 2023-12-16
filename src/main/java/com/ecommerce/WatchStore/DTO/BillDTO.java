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
    public float totalPrice;
    private String createdBy;
    private LocalDateTime updateDate;
    private String updatedBy;
    private String status;
    private List<BillDetailDTO> billDetailDTOList;

}