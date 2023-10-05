package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Product> createProduct(@RequestBody Product product,@RequestParam Long id ){
        try{
            Product saveProduct = productService.createProduct(product, id);
            return ResponseEntity.status(HttpStatus.CREATED).body(saveProduct);
        }catch (BrandNotFoundException e   ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
