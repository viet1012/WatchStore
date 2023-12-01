package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.ReceiptDetailDTO;
import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import com.ecommerce.WatchStore.Services.ReceiptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Admin/Receipt-details")
public class ReceiptDetailController {
    @Autowired
    private ReceiptDetailService receiptDetailService;

    @GetMapping("/GetAll")
    public ResponseEntity<List<ReceiptDetailDTO>> getAllReceiptDetails() {
        List<ReceiptDetailDTO> receiptDetails = receiptDetailService.getAllReceiptDetails();
        return new ResponseEntity<>(receiptDetails, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptDetail> getReceiptDetailById(@PathVariable Long id) {
        Optional<ReceiptDetail> receiptDetail = receiptDetailService.getReceiptDetailById(id);

        return receiptDetail.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Create")
    public ResponseEntity<ReceiptDetailDTO> createReceiptDetail(@RequestBody ReceiptDetail receiptDetail, @RequestParam long receiptId, @RequestParam long productId) {
        ReceiptDetail createdReceiptDetail = receiptDetailService.createReceiptDetail(receiptDetail, receiptId ,productId);

        ReceiptDetailDTO receiptDetailDTO = new ReceiptDetailDTO();
        receiptDetailDTO.setId(createdReceiptDetail.getId());
        receiptDetailDTO.setReceiptId(createdReceiptDetail.getReceipt().getId());
        receiptDetailDTO.setProductId(createdReceiptDetail.getProduct().getProductId());
        receiptDetailDTO.setQuantity(createdReceiptDetail.getQuantity());
        receiptDetailDTO.setPrice(createdReceiptDetail.getPrice());

        return new ResponseEntity<>(receiptDetailDTO, HttpStatus.CREATED);
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
