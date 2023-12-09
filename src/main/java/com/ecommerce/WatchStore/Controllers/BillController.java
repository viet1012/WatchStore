package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.DTO.BillPageDTO;
import com.ecommerce.WatchStore.DTO.SupplierPageDTO;
import com.ecommerce.WatchStore.Entities.*;
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

    @GetMapping("/Items")
    public ResponseEntity<ResponseWrapper<BillPageDTO>> getSuppliers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<Bill> bills = billService.getBillsByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalBills = billService.getTotalBills();
        int totalPages = (int) Math.ceil(totalBills / (double) pageSize);

        BillPageDTO billPageDTO = new BillPageDTO(bills, page, pageSize, totalPages);

        ResponseWrapper<BillPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, billPageDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/GetAll")
//    public ResponseEntity<ResponseWrapper<List<Bill>>> getBillList() {
//        List<Bill> billList = billService.getBillList();
//        long totalBills = billService.getTotalBills();
//
//        if (billList.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//
//        ResponseWrapper<List<Bill>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalBills, billList);
//        return ResponseEntity.ok(response);
//    }

    public ResponseEntity<ResponseWrapper<List<BillDTO>>> getBillList() {
        List<BillDTO> billList = billService.getAll();
        long totalBills = billService.getTotalBills();

        if (billList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ResponseWrapper<List<BillDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalBills, billList);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/GetBillByUserId")
    public ResponseEntity<ResponseWrapper<List<BillDTO>>> getBillFromUserId(@RequestBody BillDTO billDTO) {
        List<BillDTO> billList = billService.getBillFromUserId(billDTO.getUserId());

        if (billList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ResponseWrapper<List<BillDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true,  billList);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/GetBillDetailByUserId")
    public ResponseEntity<ResponseWrapper<List<BillDTO>>> getBillDetailFromUserId(@RequestBody BillDTO billDTO) {
        List<BillDTO> billList = billService.getBillDetailFromUserId(billDTO.getUserId());

        if (billList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ResponseWrapper<List<BillDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true,  billList);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/Apply-voucher/{id}")
    public ResponseEntity<Object> applyVoucherToBill(
            @PathVariable Long id,
            @RequestParam Long voucherId) {

        try {
            voucherService.applyVoucherDiscount(id, voucherId);
            return ResponseEntity.ok().build(); // Trả về hóa đơn sau khi áp dụng voucher thành công
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Trả về lỗi khi áp dụng voucher không thành công
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // Trả về lỗi khi không tìm thấy hóa đơn
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Trả về lỗi nội bộ server nếu có lỗi khác
        }
    }

//    @PostMapping("/Create")
//    public ResponseEntity<ResponseWrapper<Bill>> createBill(@RequestBody Bill billRequest, @RequestParam Long userId, @RequestParam(required = false, defaultValue = "1") Long voucherId) {
//        Bill savedBill = billService.createBill(billRequest, userId, voucherId);
//        ResponseWrapper<Bill> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Created", true, savedBill);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @PostMapping("/Create")
    public ResponseEntity<Bill> createBill(@RequestBody BillDTO billRequest) {
        Bill savedBill = billService.createBill(billRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBill);
    }

    @PostMapping("/CancelOrder/{id}")
    public ResponseEntity<Bill> cancelBill(@PathVariable Long billId) {
        Bill savedBill = billService.cancelbill(billId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedBill);
    }

    @PutMapping("/Update-Status")
    public ResponseEntity<ResponseWrapper<Bill>> updateBillStatus( @RequestBody Bill Bill) {

        // Lưu hóa đơn đã cập nhật
        Bill updatedBill = billService.updateBillStatus(Bill.getId(), Bill.getStatus());

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

