package com.ecommerce.WatchStore.Controllers;
import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.Entities.Brand;
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
    public  ResponseEntity<List<Brand>> getAllBrands(){
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Brand> getBrandById(@PathVariable int id){
        Optional<Brand> brand = brandService.getBrandById(id);
        if(brand.isPresent()){
            return  ResponseEntity.ok(brand.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/Create")
    public  ResponseEntity<Brand> createBrand(@RequestBody BrandDTO brand){
        Brand savedBrand = brandService.saveBrand(brand);
        return  ResponseEntity.status(HttpStatus.CREATED).body(savedBrand);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable int id, @RequestBody Brand updateBrand){
       try{
           updateBrand.setIdBrand(id);
           Brand brand = brandService.updateBrand(updateBrand, id);
           return ResponseEntity.ok(brand);
       }catch (RuntimeException e){
           return ResponseEntity.notFound().build();
       }
    }
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteBrandById(@PathVariable int id){
        brandService.deleteBrandById(id);
        return ResponseEntity.noContent().build();
    }
}
