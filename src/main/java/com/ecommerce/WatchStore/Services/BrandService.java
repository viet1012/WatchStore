package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
        @Autowired
        private  BrandRepository brandRepository;
        public List<Brand> getAllBrands(){
            return brandRepository.findAll();
        }
        public Optional<Brand> getBrandById(Long id){
            return brandRepository.findById(id);
        }

    public Brand saveBrand(BrandDTO newBrand) {
        // Kiểm tra xem tên brand đã tồn tại chưa
        Optional<Brand> existingBrand = brandRepository.findBrandByName(newBrand.getName());
        if (existingBrand.isPresent()) {
            throw new RuntimeException("Tên thương hiệu đã tồn tại.");
        }

        Brand brand = new Brand();
        brand.setActive(newBrand.isActive());
        brand.setCreatedBy(newBrand.getCreatedBy());
        brand.setCreatedDt(newBrand.getCreatedDt());
        brand.setName(newBrand.getName());
        brand.setUpdatedBy(newBrand.getUpdatedBy());
        brand.setUpdatedDt(newBrand.getUpdatedDt());

        return brandRepository.save(brand);
    }

    public Brand updateBrand(Brand updatedBrand , Long id){
            Optional<Brand> existingBrandOptional = brandRepository.findById(updatedBrand.getIdBrand());
            if (existingBrandOptional.isPresent()){
                Brand existingBrand = existingBrandOptional.get();
                existingBrand.setName(updatedBrand.getName());
                return brandRepository.save(existingBrand);
            }
           else
            {
                throw  new RuntimeException("Không tìm thấy thương hiệu với ID: " + updatedBrand.getIdBrand());
            }
            
        }
        public void deleteBrandById(Long id){
             brandRepository.deleteById( id);
        }


}
