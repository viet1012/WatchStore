package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.Entities.Brand;
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
        ResponseWrapper<List<Brand>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Brands retrieved successfully", true, brands);
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
    public ResponseEntity<ResponseWrapper<Brand>> updateBrand(@PathVariable int id, @RequestBody Brand updateBrand) {
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
    public ResponseEntity<Void> deleteBrandById(@PathVariable int id) {
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}
