package com.ecommerce.WatchStore.Controllers;


import com.ecommerce.WatchStore.DTO.ReceiptDTO;
import com.ecommerce.WatchStore.DTO.ReceiptDetailDTO;
import com.ecommerce.WatchStore.DTO.ReceiptPageDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Receipt;
import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import com.ecommerce.WatchStore.Entities.Supplier;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Admin/Receipt")
public class ReceiptController {
    @Autowired
    private ReceiptService receiptService;

    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/GetAllReceipts")
    public ResponseEntity<ResponseWrapper<List<Receipt>>> GetAllReceipts() {
        List<Receipt> receipts = receiptService.getAllReceipts();
        long totalReceipts = receiptService.getTotalReceipts();
        ResponseWrapper<List<Receipt>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Brands retrieved successfully", true, totalReceipts, receipts);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<ReceiptDTO>>> getAll() {
        List<ReceiptDTO> receipts = receiptService.getAllReceiptDetailsWithTotalAndSupplierId();
        ResponseWrapper<List<ReceiptDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Brands retrieved successfully", true, receipts);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/Items")
    public ResponseEntity<ResponseWrapper<ReceiptPageDTO>> getReceipts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<Receipt> receipts = receiptService.getReceiptsByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalReceipts = receiptService.getTotalReceipts();
        int totalPages = (int) Math.ceil(totalReceipts / (double) pageSize);

        ReceiptPageDTO receiptPageDTO = new ReceiptPageDTO(receipts, page, pageSize, totalPages);

        ResponseWrapper<ReceiptPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, receiptPageDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Receipt>> getReceiptById(@PathVariable Long id) {
        Optional<Receipt> receipt = receiptService.getReceiptById(id);

        if (receipt.isPresent()) {
            ResponseWrapper<Receipt> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, receipt.get());
            return ResponseEntity.ok(response);
        } else {
            ResponseWrapper<Receipt> response = new ResponseWrapper<>(HttpStatus.NOT_FOUND.value(), "Receipt not found", false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

//    @PostMapping("/Create")
//    public ResponseEntity<ResponseWrapper<Receipt>> createReceipt(@RequestBody Receipt receipt, @RequestParam Long userId, @RequestParam Long supplierId) {
//        Receipt createdReceipt = receiptService.createReceipt(receipt, userId, supplierId);
//        ResponseWrapper<Receipt> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Receipt created successfully", true, createdReceipt);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @PostMapping("/Create")
    public ResponseEntity<?> createReceipt(
            @RequestBody ReceiptDTO receiptDTO
    ) {
        try {
            Receipt createdReceipt = receiptService.createReceipt(
                    receiptDTO

            );
            return ResponseEntity.ok("Receipt created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create receipt " + e.getMessage());
        }
    }


    @PutMapping("/Update/{id}")
    public ResponseEntity<ResponseWrapper<Receipt>> updateReceipt(@PathVariable Long id, @RequestBody Receipt updatedReceipt) {
        Receipt updated = receiptService.updateReceipt(id, updatedReceipt);

        if (updated != null) {
            ResponseWrapper<Receipt> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Receipt updated successfully", true, updated);
            return ResponseEntity.ok(response);
        } else {
            ResponseWrapper<Receipt> response = new ResponseWrapper<>(HttpStatus.NOT_FOUND.value(), "Receipt not found", false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteReceipt(@PathVariable Long id) {
        receiptService.deleteReceipt(id);
        ResponseWrapper<Void> response = new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Receipt deleted successfully", true, null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
