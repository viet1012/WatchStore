package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Receipt;
import com.ecommerce.WatchStore.Repositories.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    public Optional<Receipt> getReceiptById(Long id) {
        return receiptRepository.findById(id);
    }

    public Receipt createReceipt(Receipt receipt) {
        // Thực hiện các kiểm tra hoặc xử lý trước khi lưu vào cơ sở dữ liệu
        return receiptRepository.save(receipt);
    }

    public Receipt updateReceipt(Long id, Receipt updatedReceipt) {
        // Kiểm tra xem Receipt có tồn tại không
        Optional<Receipt> existingReceipt = receiptRepository.findById(id);

        if (existingReceipt.isPresent()) {
            Receipt receipt = existingReceipt.get();
            // Cập nhật thông tin của Receipt
            receipt.setSupplier(updatedReceipt.getSupplier());
            receipt.setUser(updatedReceipt.getUser());
            receipt.setTotal(updatedReceipt.getTotal());
            receipt.setUpdatedBy(updatedReceipt.getUpdatedBy());
            receipt.setActive(updatedReceipt.isActive());

            return receiptRepository.save(receipt);
        } else {
            // Xử lý khi Receipt không tồn tại
            return null;
        }
    }

    public void deleteReceipt(Long id) {
        // Xóa Receipt theo ID
        receiptRepository.deleteById(id);
    }
}
