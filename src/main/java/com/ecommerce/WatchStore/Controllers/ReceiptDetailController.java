package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import com.ecommerce.WatchStore.Services.ReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Admin/receipt-details")
public class ReceiptDetailController {
    @Autowired
    private ReceiptDetailService receiptDetailService;

    @GetMapping("/GetAll")
    public ResponseEntity<List<ReceiptDetail>> getAllReceiptDetails() {
        List<ReceiptDetail> receiptDetails = receiptDetailService.getAllReceiptDetails();
        return new ResponseEntity<>(receiptDetails, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDetail> getReceiptDetailById(@PathVariable Long id) {
        Optional<ReceiptDetail> receiptDetail = receiptDetailService.getReceiptDetailById(id);

        return receiptDetail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Create")
    public ResponseEntity<ReceiptDetail> createReceiptDetail(@RequestBody ReceiptDetail receiptDetail) {
        ReceiptDetail createdReceiptDetail = receiptDetailService.createReceiptDetail(receiptDetail);
        return new ResponseEntity<>(createdReceiptDetail, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<ReceiptDetail> updateReceiptDetail(@PathVariable Long id, @RequestBody ReceiptDetail updatedReceiptDetail) {
        ReceiptDetail updated = receiptDetailService.updateReceiptDetail(id, updatedReceiptDetail);

        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteReceiptDetail(@PathVariable Long id) {
        receiptDetailService.deleteReceiptDetail(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
