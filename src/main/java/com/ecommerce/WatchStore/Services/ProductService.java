package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Common.ProductNotFoundException;
import com.ecommerce.WatchStore.Entities.Accessory;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Category;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.AccessoryRepository;
import com.ecommerce.WatchStore.Repositories.BrandRepository;
import com.ecommerce.WatchStore.Repositories.CategoryRepository;
import com.ecommerce.WatchStore.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private FileStorageService fileStorageService;


    private String formatFileNames(List<MultipartFile> imageFiles) {
        StringBuilder imageFileNames = new StringBuilder();
        for (MultipartFile imageFile : imageFiles) {
            String fileName = fileStorageService.storeFile(imageFile);
            if (fileName != null) {
                if (imageFileNames.length() > 0) {
                    imageFileNames.append(", ");
                }
                imageFileNames.append(fileName);
            }
        }
        return imageFileNames.toString();
    }

    public Optional<Brand> getBrandByProduct(Product product){
        return brandService.getBrandById(product.getBrand().getIdBrand());
    }
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }


    public Product createProduct(Product product, int brandId, Long categoryId, Long accessoryId, List<MultipartFile> imageFiles ,List<MultipartFile> thumnailImgFiles ) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new ProductNotFoundException("Sản phẩm đã tồn tại với tên: " + product.getProductName());
        }
        Optional<Brand> brandOptional = brandRepository.findById(brandId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        Optional<Accessory> accessoryOptional = accessoryRepository.findById(accessoryId);

        if (brandOptional.isPresent() && categoryOptional.isPresent()) {
            Brand brand = brandOptional.get();
            Category category = categoryOptional.get();
            Accessory accessory = accessoryOptional.get();

            Product newProduct = new Product();
            newProduct.setCategory(category);
            newProduct.setBrand(brand);
            newProduct.setActive(product.getActive());
            newProduct.setCreatedBy(product.getCreatedBy());
            newProduct.setProductName(product.getProductName());
            newProduct.setPrice(product.getPrice());
            newProduct.setQuantity(product.getQuantity());
            newProduct.setAccessory(accessory);
            newProduct.setImg(formatFileNames(imageFiles));
            newProduct.setThumbnail(formatFileNames(thumnailImgFiles));
            newProduct.setGender(product.getGender());
            newProduct.setCode(product.getCode());
            newProduct.setColor(product.getColor());
            newProduct.setDescription(product.getDescription());
            newProduct.setStatus(product.getStatus());
            return productRepository.save(newProduct);
        } else {
            throw new BrandNotFoundException("Không tìm thấy thương hiệu với ID: " + brandId);
        }
    }


    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long productId, Product updatedProduct, Long brandId, List<MultipartFile> updateImage) {
        Optional<Product> existingProductOptional = productRepository.findById(productId);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setImg(formatFileNames(updateImage)); // Cập nhật ảnh với danh sách mới
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            existingProduct.setCode(updatedProduct.getCode());
            existingProduct.setColor(updatedProduct.getColor());
            // Kiểm tra brand có tồn tại hay không
            Optional<Brand> optionalBrand = brandRepository.findById(Math.toIntExact(brandId));
            if (optionalBrand.isPresent()) {
                Brand brand = optionalBrand.get();
                existingProduct.setBrand(brand);
            } else {
                throw new BrandNotFoundException("Không tìm thấy thương hiệu với ID: " + brandId);
            }

            return productRepository.save(existingProduct);
        } else {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }
    }

    public List<Product> getAllProductsByPriceAsc() {

        return productRepository.findAllProductByPriceAsc();
    }

    public List<Product> getAllProductsByPriceDesc() {
        return productRepository.findAllProductByPriceDesc();
    }

    public long getTotalProducts() {
        return productRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số sản phẩm.
    }

    public List<Product> getProductsByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.getContent(); // Lấy danh sách sản phẩm trên trang cụ thể.

    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByProductNameContaining(name);
    }
    public Product getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            // Nếu không tìm thấy sản phẩm, bạn có thể ném một ngoại lệ hoặc xử lý theo cách khác tùy ý.
            throw new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + id);
        }
    }
}

