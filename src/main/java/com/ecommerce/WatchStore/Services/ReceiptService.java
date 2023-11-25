package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Receipt;
import com.ecommerce.WatchStore.Entities.Supplier;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.ReceiptRepository;
import com.ecommerce.WatchStore.Repositories.SupplierRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    public Optional<Receipt> getReceiptById(Long id) {
        return receiptRepository.findById(id);
    }

    public Receipt createReceipt(Receipt receipt, long userId, long supplierId) {
        // Thực hiện các kiểm tra hoặc xử lý trước khi lưu vào cơ sở dữ liệu
        Optional<Supplier> supplier = supplierRepository.findById(supplierId);
        Optional<User> userOptional = userRepository.findById(userId);

        Receipt newReceipt = new Receipt();
        newReceipt.setTotal(receipt.getTotal());
        newReceipt.setSupplier(supplier.get());
        newReceipt.setCreatedBy(userOptional.get().getDisplayName());
        newReceipt.setUser(userOptional.get());

        return receiptRepository.save(newReceipt);
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
    public List<Receipt> getReceiptsByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Receipt> receiptPage = receiptRepository.findAll(pageable);
        return receiptPage.getContent(); // Lấy danh sách receipt trên trang cụ thể.
    }

    public long getTotalReceipts() {
        return receiptRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số receipt.
    }
}
