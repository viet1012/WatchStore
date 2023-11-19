package com.ecommerce.WatchStore.Controllers;
import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.DTO.BillDetailDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
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

    @GetMapping("/GetAll")
    public ResponseEntity<List<BillDetail>> getListBillDetail(){
        List<BillDetail> billDetailList =  billDetailService.getBillDetail();
        return ResponseEntity.ok(billDetailList);
    }
    @PostMapping("/Create")
    public ResponseEntity<BillDetail> createBillDetail(@RequestBody BillDetail billDetail, @RequestParam Long billId,@RequestParam Long productId) {

        BillDetail createdBillDetail = billDetailService.createBillDetail(billDetail, billId, productId);

        return new ResponseEntity<>(createdBillDetail, HttpStatus.CREATED);
    }
}
