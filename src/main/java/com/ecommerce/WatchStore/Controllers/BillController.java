package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Services.BillService;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/User/ills")
public class BillController {

    @Autowired
    private BillService billService;
    @GetMapping("/GetAll")
    public ResponseEntity<List<Bill>> getBillList(){
        List<Bill> billList = billService.getBillList();

        if (billList.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về mã 204 No Content nếu danh sách rỗng.
        }
        return ResponseEntity.ok(billList);
    }

    @PostMapping("/Create")
    public ResponseEntity<Bill> createBill(@RequestBody BillDTO billRequest, @RequestParam Long userId, @RequestParam Long voucherId) {

        Bill savedBill =  billService.createBill(billRequest, userId, voucherId);
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedBill);
    }


    @PutMapping("/Update/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id, @RequestBody BillDTO billDTO) {
        Bill existingBill = billService.getBillById(id);
        if (existingBill == null) {
            return ResponseEntity.notFound().build();
        }

        // Cập nhật thông tin của hóa đơn từ billDTO
        existingBill.setDeliverAddress(billDTO.getDeliverAddress());
        existingBill.setActive(true);
        existingBill.setUpdatedBy(billDTO.getUpdatedBy());
        existingBill.setUpdatedDate(billDTO.getUpdateDate());

        // Lưu hóa đơn đã cập nhật
        Bill updatedBill = billService.updateBill(id,existingBill);

        return new ResponseEntity<>(updatedBill, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        if (bill != null) {
            return ResponseEntity.ok(bill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

