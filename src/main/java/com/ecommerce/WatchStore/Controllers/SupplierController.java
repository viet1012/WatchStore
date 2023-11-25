package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.CategoryPageDTO;
import com.ecommerce.WatchStore.DTO.SupplierPageDTO;
import com.ecommerce.WatchStore.Entities.Category;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.Supplier;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Admin/Supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<Supplier>>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        long totalSuppliers = supplierService.getTotalSuppliers();
        ResponseWrapper<List<Supplier>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalSuppliers, suppliers);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<SupplierPageDTO>> getSuppliers(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<Supplier> suppliers = supplierService.getSuppliersByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalSuppliers = supplierService.getTotalSuppliers();
        int totalPages = (int) Math.ceil(totalSuppliers / (double) pageSize);

        SupplierPageDTO supplierPageDTO = new SupplierPageDTO(suppliers, page, pageSize, totalPages);

        ResponseWrapper<SupplierPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, supplierPageDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Supplier>> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);

        return supplier.map(value -> {
            ResponseWrapper<Supplier> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Supplier retrieved successfully", true, value);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<Supplier>> createSupplier(@RequestBody Supplier supplier) {
        Supplier createdSupplier = supplierService.createSupplier(supplier);
        ResponseWrapper<Supplier> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Supplier created successfully", true, createdSupplier);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<ResponseWrapper<Supplier>> updateSupplier(@PathVariable Long id, @RequestBody Supplier updatedSupplier) {
        Supplier updated = supplierService.updateSupplier(id, updatedSupplier);

        if (updated != null) {
            ResponseWrapper<Supplier> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Supplier updated successfully", true, updated);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        ResponseWrapper<Void> response = new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Supplier deleted successfully", true, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/Delete-multiple")
    public ResponseEntity<ResponseWrapper<Void>> deleteSuppliers(@RequestBody List<Long> supplierIds) {
        for (Long supplierId : supplierIds) {
            supplierService.deleteSupplier(supplierId);
        }
        ResponseWrapper<Void> response = new ResponseWrapper<>(HttpStatus.NO_CONTENT.value(), "Suppliers deleted successfully", true, null);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
