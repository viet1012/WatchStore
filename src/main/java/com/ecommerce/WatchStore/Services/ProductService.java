package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Common.ProductNotFoundException;
import com.ecommerce.WatchStore.DTO.ProductDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void saveProduct(Product product)
    {
        productRepository.save(product);
    }

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


//    public List<Product> getAllProduct(){
//        return productRepository.findAll();
//    }
    public List<ProductDTO> getAllProduct() {

//        List<Product> productList = productRepository.findAll();
        List<Product> productList = productRepository.findAllByActiveTrue();
        List<ProductDTO> productDtoList = productList.stream().map(product -> {
            ProductDTO productDto = new ProductDTO();
            productDto.setProductId(product.getProductId());
            productDto.setProductName(product.getProductName());
            productDto.setImg(product.getImg());
            productDto.setPrice(product.getPrice());

            productDto.setQuantity(product.getQuantity());

            productDto.setBrandId(product.getBrand() != null ? product.getBrand().getIdBrand() : null);
            productDto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
            productDto.setCreatedBy(product.getCreatedBy());
            productDto.setCreatedDate(product.getCreatedDate());
            productDto.setUpdatedBy(product.getUpdatedBy());
            productDto.setUpdatedDate(product.getUpdatedDate());
            productDto.setActive(product.getActive());
            productDto.setCode(product.getCode());
            productDto.setThumbnail(product.getThumbnail());
            productDto.setGender(product.getGender());
            productDto.setStatus(product.getStatus());
            productDto.setColor(product.getColor());
            productDto.setAccessoryId(product.getAccessory() != null ? product.getAccessory().getId() : null);
            productDto.setDescription(product.getDescription());

            return productDto;
        }).collect(Collectors.toList());

        return productDtoList;
    }
    public Product createProduct(Product product, long brandId, Long categoryId,  List<MultipartFile> imageFiles ,List<MultipartFile> thumnailImgFiles ) {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new ProductNotFoundException("Sản phẩm đã tồn tại với tên: " + product.getProductName());
        }
        Optional<Brand> brandOptional = brandRepository.findById(brandId);
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (brandOptional.isPresent() && categoryOptional.isPresent()) {
            Brand brand = brandOptional.get();
            Category category = categoryOptional.get();


            Product newProduct = new Product();
            newProduct.setCategory(category);
            newProduct.setBrand(brand);
            newProduct.setActive(true);
            newProduct.setCreatedBy(product.getCreatedBy());
            newProduct.setProductName(product.getProductName());
            newProduct.setPrice(0);
            newProduct.setQuantity(0);
            newProduct.setAccessory(null);
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

    public List<Product> deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            Product currentProduct = productOptional.get();
            currentProduct.setActive(false);
            productRepository.save(currentProduct);
        } else {
            throw new NoSuchElementException("Không tìm thấy sản phẩm với ID: " + id);
        }

        // Lấy danh sách tất cả sản phẩm có trạng thái active = true
        return productRepository.findAllByActiveTrue();
    }

    public List<Product> deleteMultipleProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        for (Product product : products) {
            product.setActive(false);
        }

        productRepository.saveAll(products);

        // Lấy danh sách tất cả sản phẩm có trạng thái active = true
        return productRepository.findAllByActiveTrue();
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
            existingProduct.setActive(updatedProduct.getActive());
            existingProduct.setImg(formatFileNames(updateImage));
            // Kiểm tra brand có tồn tại hay không
            Optional<Brand> optionalBrand = brandRepository.findById(brandId);
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
    public List<ProductDTO> getProductsDTOByPage(int page, int pageSize) {
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDTO> productDTOList = productPage.map(product -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setProductName(product.getProductName());
            productDTO.setImg(product.getImg());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setBrandId(product.getBrand() != null ? product.getBrand().getIdBrand() : null);
            productDTO.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
            productDTO.setCreatedBy(product.getCreatedBy());
            productDTO.setCreatedDate(product.getCreatedDate());
            productDTO.setUpdatedBy(product.getUpdatedBy());
            productDTO.setUpdatedDate(product.getUpdatedDate());
            productDTO.setActive(product.getActive());
            productDTO.setCode(product.getCode());
            productDTO.setThumbnail(product.getThumbnail());
            productDTO.setGender(product.getGender());
            productDTO.setStatus(product.getStatus());
            productDTO.setColor(product.getColor());
            productDTO.setAccessoryId(product.getAccessory() != null ? product.getAccessory().getId() : null);
            productDTO.setDescription(product.getDescription());

            return productDTO;
        }).toList();

        return productDTOList;
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

