package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Supplier;
import com.ecommerce.WatchStore.Repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public long countSuppliers() {
        return supplierRepository.count();
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        // Kiểm tra xem Supplier có tồn tại không
        Optional<Supplier> existingSupplier = supplierRepository.findById(id);

        if (existingSupplier.isPresent()) {
            Supplier supplier = existingSupplier.get();
            // Cập nhật thông tin của Supplier
            supplier.setName(updatedSupplier.getName());
            supplier.setAddress(updatedSupplier.getAddress());
            supplier.setPhoneNumber(updatedSupplier.getPhoneNumber());
            supplier.setEmail(updatedSupplier.getEmail());
            supplier.setUpdatedBy(updatedSupplier.getUpdatedBy());
            supplier.setActive(updatedSupplier.isActive());

            return supplierRepository.save(supplier);
        } else {
            // Xử lý khi Supplier không tồn tại
            return null;
        }
    }

    public void deleteSupplier(Long id) {
        // Xóa Supplier theo ID
        supplierRepository.deleteById(id);
    }
    public List<Supplier> getSuppliersByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Supplier> supplierPage = supplierRepository.findAll(pageable);
        return supplierPage.getContent(); // Lấy danh sách supplier trên trang cụ thể.
    }

    public long getTotalSuppliers() {
        return supplierRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số supplier.
    }

}
