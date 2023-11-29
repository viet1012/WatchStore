package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Common.ProductNotFoundException;
import com.ecommerce.WatchStore.DTO.ProductDTO;
import com.ecommerce.WatchStore.DTO.ProductPageDTO;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/Product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProduct();
        long totalProducts = productService.getTotalProducts();
        ResponseWrapper<List<ProductDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalProducts, products);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/Create", consumes = "multipart/form-data")
    public ResponseEntity<ResponseWrapper<Product>> createProduct(
            @RequestParam("brandId") int brandId,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("imageFile") List<MultipartFile> imageFile,
            @RequestParam("thumnail") List<MultipartFile> thumnailImgFiles,
            @ModelAttribute Product product) {
        Product createdProduct = productService.createProduct(product, brandId, categoryId, imageFile, thumnailImgFiles);
        ResponseWrapper<Product> response = new ResponseWrapper<>(HttpStatus.CREATED.value(), "Product created successfully", true, createdProduct);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/Update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ResponseWrapper<Product>> updateProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct, @RequestParam Long idBrand, List<MultipartFile> file) {
        try {
            Product product = productService.updateProduct(id, updatedProduct, idBrand, file);
            ResponseWrapper<Product> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Product updated successfully", true, product);
            return ResponseEntity.ok(response);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BrandNotFoundException e) {
            ResponseWrapper<Product> response = new ResponseWrapper<>(HttpStatus.BAD_REQUEST.value(), "Brand not found with ID: " + idBrand, false, null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<ResponseWrapper<List<Product>>> deleteProduct(@PathVariable Long id) {
        List<Product> products = productService.deleteProduct(id);
        ResponseWrapper<List<Product>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Deleted product successfully", true, products);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/Delete-multiple")
    public ResponseEntity<ResponseWrapper<List<Product>>> deleteMultipleProducts(@RequestBody List<Long> productIds) {
        List<Product> products = productService.deleteMultipleProducts(productIds);
        ResponseWrapper<List<Product>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Deleted product successfully", true, products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Items")
    public ResponseEntity<ResponseWrapper<ProductPageDTO>> getProducts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {

        //  List<Product> products = productService.getProductsByPage(page, pageSize);
        List<ProductDTO> products = productService.getProductsDTOByPage(page, pageSize);

        // Tính toán thông tin phân trang
        long totalProducts = productService.getTotalProducts();
        int totalPages = (int) Math.ceil(totalProducts / (double) pageSize);

        ProductPageDTO productPageDTO = new ProductPageDTO(products, page, pageSize, totalPages);

        ResponseWrapper<ProductPageDTO> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, productPageDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Price-asc")
    public ResponseEntity<ResponseWrapper<List<Product>>> getAllProductsByPriceAsc() {
        List<Product> products = productService.getAllProductsByPriceAsc();
        ResponseWrapper<List<Product>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Price-desc")
    public ResponseEntity<ResponseWrapper<List<Product>>> getAllProductsByPriceDesc() {
        List<Product> products = productService.getAllProductsByPriceDesc();
        ResponseWrapper<List<Product>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<List<Product>>> searchProducts(
            @RequestParam(name = "name", required = false) String productName,
            @RequestParam(name = "id", required = false) Long productId) {

        List<Product> products;

        if (productName != null) {
            products = productService.searchProductsByName(productName);
        } else if (productId != null) {
            Product product = productService.getProductById(productId);
            if (product != null) {
                products = Collections.singletonList(product);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            // Trường hợp không có tham số nào được cung cấp,  có thể trả về danh sách trống hoặc thông báo lỗi tùy ý.
            products = Collections.emptyList();
        }

        ResponseWrapper<List<Product>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, products);
        return ResponseEntity.ok(response);
    }

}
