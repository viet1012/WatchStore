package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Voucher;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.BillService;
import com.ecommerce.WatchStore.Services.VoucherService;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/User/Bill")
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private VoucherService voucherService;
    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<Bill>>> getBillList() {
        List<Bill> billList = billService.getBillList();

        if (billList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ResponseWrapper<List<Bill>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, billList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Apply-voucher/{id}")
    public ResponseEntity<Object> applyVoucherToBill(
            @PathVariable Long id,
            @RequestParam Long voucherId) {

        try {
            Bill updatedBill = voucherService.applyVoucherDiscount(id, voucherId);
            return ResponseEntity.ok(updatedBill); // Trả về hóa đơn sau khi áp dụng voucher thành công
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Trả về lỗi khi áp dụng voucher không thành công
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // Trả về lỗi khi không tìm thấy hóa đơn
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Trả về lỗi nội bộ server nếu có lỗi khác
        }
    }
    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<Bill>> createBill(@RequestBody Bill billRequest, @RequestParam Long userId, @RequestParam(required = false, defaultValue = "1") Long voucherId ) {
        Bill savedBill =  billService.createBill(billRequest, userId, voucherId);
        ResponseWrapper<Bill> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Created", true, savedBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/Update-Status/{id}")
    public ResponseEntity<ResponseWrapper<Bill>> updateBillStatus(@PathVariable Long id, @RequestBody Bill Bill) {

        // Lưu hóa đơn đã cập nhật
        Bill updatedBill = billService.updateBillStatus(id, Bill.getStatus());

        ResponseWrapper<Bill> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Updated", true, updatedBill);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/Update/{id}")
    public ResponseEntity<ResponseWrapper<Bill>> updateBill(@PathVariable Long id, @RequestBody BillDTO billDTO) {
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
        Bill updatedBill = billService.updateBill(id, existingBill);

        ResponseWrapper<Bill> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Updated", true, updatedBill);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Bill>> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        if (bill != null) {
            ResponseWrapper<Bill> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, bill);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

