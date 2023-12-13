package com.ecommerce.WatchStore.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListBillDetailDTO {
    private List<BillDetailDTO> billDetailDTOList;

    public ListBillDetailDTO() {
        this.billDetailDTOList = new ArrayList<>(); // Khởi tạo danh sách khi tạo đối tượng ListBillDetailDTO
    }

    public List<BillDetailDTO> getBillDetailDTOList() {
        return billDetailDTOList;
    }

    public void setBillDetailDTOList(List<BillDetailDTO> billDetailDTOList) {
        this.billDetailDTOList = billDetailDTOList;
    }
}
