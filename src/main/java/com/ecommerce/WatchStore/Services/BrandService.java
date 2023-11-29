package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BrandDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.BrandRepository;
import com.ecommerce.WatchStore.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Brand> getAllBrands() {
//        return brandRepository.findAll();
        return brandRepository.findAllByActiveTrue();
    }

    public Optional<Brand> getBrandById(long id) {
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

    public Brand updateBrand(Brand updatedBrand, long id) {
        Optional<Brand> existingBrandOptional = brandRepository.findById(id);
        if (existingBrandOptional.isPresent()) {
            Brand existingBrand = existingBrandOptional.get();
            existingBrand.setName(updatedBrand.getName());
            existingBrand.setUpdatedDt(updatedBrand.getUpdatedDt());
            existingBrand.setUpdatedBy(updatedBrand.getUpdatedBy());
            return brandRepository.save(existingBrand);
        } else {
            throw new RuntimeException("Không tìm thấy thương hiệu với ID: " + updatedBrand.getIdBrand());
        }

    }

    public List<Brand> deleteBrand(Long id) {
        Optional<Brand> brandOptional = brandRepository.findById(id);

        if (brandOptional.isPresent()) {
            Brand currentBrand = brandOptional.get();
            currentBrand.setActive(false);
            brandRepository.save(currentBrand);
        } else {
            throw new NoSuchElementException("Không tìm thấy thương hiệu với ID: " + id);
        }

        // Lấy danh sách tất cả thương hiệu có trạng thái active = true
        return brandRepository.findAllByActiveTrue();
    }

    public List<Brand> deleteMultipleBrands(List<Long> brandIds) {
        List<Brand> brands = brandRepository.findAllById(brandIds);

        for (Brand brand : brands) {
            brand.setActive(false);
        }

        brandRepository.saveAll(brands);

        // Lấy danh sách tất cả thương hiệu có trạng thái active = true
        return brandRepository.findAllByActiveTrue();
    }
    public List<Brand> getBrandsByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Brand> brandPage = brandRepository.findAll(pageable);
        return brandPage.getContent(); // Lấy danh sách sản phẩm trên trang cụ thể.

    }

    public long getTotalBrands() {
        return brandRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số sản phẩm.
    }
}
