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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/listProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products =  productService.getAllProduct();
        return ResponseEntity.ok(products);
    }
    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody Product product,@RequestParam int brandId, @RequestParam long categoryId ){
        try{
            Product saveProduct = productService.createProduct(product, brandId,categoryId );
            return ResponseEntity.status(HttpStatus.CREATED).body(saveProduct);
        }catch (BrandNotFoundException e   ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, @RequestParam Long idBrand) {
        try {
            Product product = productService.updateProduct(id, updatedProduct, idBrand);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BrandNotFoundException e) {
            return ResponseEntity.badRequest().body("Không tìm thấy thương hiệu với ID: " + idBrand);
        }
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id ){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<ProductPageDTO> getProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        List<Product> products = productService.getProductsByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalProducts = productService.getTotalProducts();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

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
