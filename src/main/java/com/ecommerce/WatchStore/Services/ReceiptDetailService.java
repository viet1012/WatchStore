package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import com.ecommerce.WatchStore.Repositories.ReceiptDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptDetailService {
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    public List<ReceiptDetail> getAllReceiptDetails() {
        return receiptDetailRepository.findAll();
    }

    public Optional<ReceiptDetail> getReceiptDetailById(Long id) {
        return receiptDetailRepository.findById(id);
    }

    public ReceiptDetail createReceiptDetail(ReceiptDetail receiptDetail) {
        // Thực hiện các kiểm tra hoặc xử lý trước khi lưu vào cơ sở dữ liệu
        return receiptDetailRepository.save(receiptDetail);
    }

    public ReceiptDetail updateReceiptDetail(Long id, ReceiptDetail updatedReceiptDetail) {
        // Kiểm tra xem ReceiptDetail có tồn tại không
        Optional<ReceiptDetail> existingReceiptDetail = receiptDetailRepository.findById(id);

        if (existingReceiptDetail.isPresent()) {
            ReceiptDetail receiptDetail = existingReceiptDetail.get();
            // Cập nhật thông tin của ReceiptDetail
            receiptDetail.setReceipt(updatedReceiptDetail.getReceipt());
            receiptDetail.setProduct(updatedReceiptDetail.getProduct());
            receiptDetail.setQuantity(updatedReceiptDetail.getQuantity());
            receiptDetail.setPrice(updatedReceiptDetail.getPrice());
            receiptDetail.setUpdatedBy(updatedReceiptDetail.getUpdatedBy());
            receiptDetail.setActive(updatedReceiptDetail.isActive());

            return receiptDetailRepository.save(receiptDetail);
        } else {
            // Xử lý khi ReceiptDetail không tồn tại
            return null;
        }
    }

    public void deleteReceiptDetail(Long id) {
        // Xóa ReceiptDetail theo ID
        receiptDetailRepository.deleteById(id);
    }
}
