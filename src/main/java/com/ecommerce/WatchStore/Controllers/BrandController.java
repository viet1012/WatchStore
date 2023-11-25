package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.DTO.BrandPageDTO;
import com.ecommerce.WatchStore.DTO.ProductPageDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<Brand>>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        long totalBrands = brandService.getTotalBrands();
        ResponseWrapper<List<Brand>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Brands retrieved successfully", true, totalBrands, brands);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Items")
    public ResponseEntity<ResponseWrapper<BrandPageDTO>> getProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<Brand> brands = brandService.getBrandsByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalBrands = brandService.getTotalBrands();
        int totalPages = (int) Math.ceil(totalBrands / (double) pageSize);

        BrandPageDTO brandPageDTO = new BrandPageDTO(brands, page, pageSize, totalPages);

        ResponseWrapper<BrandPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, brandPageDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Brand>> getBrandById(@PathVariable int id) {
        Optional<Brand> brand = brandService.getBrandById(id);

        if (brand.isPresent()) {
            ResponseWrapper<Brand> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Brand retrieved successfully", true, brand.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/Create")
    public ResponseEntity<ResponseWrapper<Brand>> createBrand(@RequestBody BrandDTO brand) {
        Brand savedBrand = brandService.saveBrand(brand);
        ResponseWrapper<Brand> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Brand created successfully", true, savedBrand);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<ResponseWrapper<Brand>> updateBrand(@PathVariable long id, @RequestBody Brand updateBrand) {
        try {
            updateBrand.setIdBrand(id);
            Brand brand = brandService.updateBrand(updateBrand, id);
            ResponseWrapper<Brand> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Brand updated successfully", true, brand);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable long id) {
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}
