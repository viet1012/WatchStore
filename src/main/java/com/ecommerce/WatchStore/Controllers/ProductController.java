package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Common.ProductNotFoundException;
import com.ecommerce.WatchStore.DTO.ProductPageDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/Product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/GetAll")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products =  productService.getAllProduct();
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/Create", consumes = "multipart/form-data")
    public ResponseEntity<Product> createProduct(
            @RequestParam("brandId") int brandId,
            @RequestParam("categoryId") long categoryId,
            @RequestParam("imageFile") List<MultipartFile> imageFile,
            @ModelAttribute Product product) {
        Product createdProduct = productService.createProduct(product, brandId, categoryId, imageFile);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/Update/{id}",  consumes = "multipart/form-data" )
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct, @RequestParam Long idBrand, List<MultipartFile> file) {
        try {
            Product product = productService.updateProduct(id, updatedProduct, idBrand, file);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BrandNotFoundException e) {
            return ResponseEntity.badRequest().body("Không tìm thấy thương hiệu với ID: " + idBrand);
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id ){
        productService.deleteProduct(id);
        return ResponseEntity.ok().body("Đã xóa sản phẩm thành công");
    }
    @DeleteMapping("/Delete-multiple")
    public ResponseEntity<?> deleteProducts(@RequestBody List<Long> productIds) {
        for (Long productId : productIds) {
            productService.deleteProduct(productId);
        }
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<ProductPageDTO> getProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize",defaultValue = "10" ) int pageSize) {

        List<Product> products = productService.getProductsByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalProducts = productService.getTotalProducts();
        int totalPages = (int) Math.ceil( totalProducts / pageSize);
        ProductPageDTO productPageDTO = new ProductPageDTO();
        productPageDTO.setProducts(products);
        productPageDTO.setCurrentPage(page);
        productPageDTO.setPageSize(pageSize);
        productPageDTO.setTotalPages(totalPages);

        return ResponseEntity.ok(productPageDTO);
    }
    @GetMapping("/price-asc")
    public ResponseEntity<List<Product>>getAllProductsByPriceAsc(){
        List<Product> products = productService.getAllProductsByPriceAsc();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/price-desc")
    public ResponseEntity<List<Product>>getAllProductsByPriceDesc(){
        List<Product> products = productService.getAllProductsByPriceDesc();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(name = "name", required = false) String productName,
            @RequestParam(name = "id", required = false) Long productId) {

        if (productName != null) {
            List<Product> products = productService.searchProductsByName(productName);
            return ResponseEntity.ok(products);
        } else if (productId != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                return ResponseEntity.ok(Collections.singletonList(product));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            // Trường hợp không có tham số nào được cung cấp,  có thể trả về danh sách trống hoặc thông báo lỗi tùy ý.
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

}
