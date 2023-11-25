package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.DTO.BillDetailDTO;
import com.ecommerce.WatchStore.DTO.BillDetailPageDTO;
import com.ecommerce.WatchStore.DTO.BillPageDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.BillDetailService;
import com.ecommerce.WatchStore.Services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/User/BillDetails")
public class BillDetailController {

    @Autowired
    private BillDetailService billDetailService;

    @GetMapping("/Items")
    public ResponseEntity<ResponseWrapper<BillDetailPageDTO>> getSuppliers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<BillDetail> billDetails = billDetailService.getBillDetailsByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalBills = billDetailService.getTotalBillDetails();
        int totalPages = (int) Math.ceil(totalBills / (double) pageSize);

        BillDetailPageDTO billDetailPageDTO = new BillDetailPageDTO(billDetails, page, pageSize, totalPages);

        ResponseWrapper<BillDetailPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, billDetailPageDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<BillDetail>>> getListBillDetail() {
        List<BillDetail> billDetailList = billDetailService.getBillDetail();
        ResponseWrapper<List<BillDetail>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, billDetailList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<BillDetail>> createBillDetail(@RequestBody BillDetail billDetail, @RequestParam Long billId, @RequestParam Long productId) {
        BillDetail createdBillDetail = billDetailService.createBillDetail(billDetail, billId, productId);
        ResponseWrapper<BillDetail> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "BillDetail created successfully", true, createdBillDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/Update/")
    public ResponseEntity<ResponseWrapper<BillDetail>> updateBillDetail(@PathVariable Long billDetailId, @RequestBody BillDetail updatedBillDetail) {
        BillDetail updatedDetail;
        try {
            updatedDetail = billDetailService.updateBillDetail(billDetailId, updatedBillDetail);
            ResponseWrapper<BillDetail> response = new ResponseWrapper<>(HttpStatus.OK.value(), "BillDetail updated successfully", true, updatedDetail);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            ResponseWrapper<BillDetail> response = new ResponseWrapper<>(HttpStatus.NOT_FOUND.value(), ex.getMessage(), false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
