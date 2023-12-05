package com.ecommerce.WatchStore.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BillDTO {
    private Long id;
    private String token;
    private Long user_id;
    private String deliverAddress;
    private LocalDateTime createDate;
    private Long voucherId;
    private float totalPrice;
    private String createdBy;
    private LocalDateTime updateDate;
    private String updatedBy;
    private List<BillDetailDTO> billDetailDTOList;

}