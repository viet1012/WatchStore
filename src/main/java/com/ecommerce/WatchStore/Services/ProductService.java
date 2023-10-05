package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Common.BrandNotFoundException;
import com.ecommerce.WatchStore.Common.ProductNotFoundException;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.BrandRepository;
import com.ecommerce.WatchStore.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;
    public Optional<Brand> getBrandByProduct(Product product){
        return brandService.getBrandById(product.getBrand().getIdBrand());
    }
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }
    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }
    public Product createProduct(Product product, Long brandId){
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new ProductNotFoundException("Sản phẩm đã tồn tại với tên: " + product.getProductName());
        }
        Optional<Brand> brandOptional   = brandRepository.findById(brandId);
        if(brandOptional .isPresent()){
            Brand brand = brandOptional.get();
            Product newProduct = new Product();
            newProduct.setBrand(brand);
            newProduct.setActive(product.getActive());
            newProduct.setCreatedBy(product.getCreatedBy());
            newProduct.setProductName(product.getProductName());
            newProduct.setPrice(product.getPrice());
            newProduct.setCreatedDate(product.getCreatedDate());
            newProduct.setImg(product.getImg());
            newProduct.setQuantity(product.getQuantity());
            return productRepository.save(newProduct);
        }
        else {
            throw new BrandNotFoundException("Không tìm thấy thương hiệu với ID: " + brandId);
        }
    }
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
    public Product updateProduct(Long productId,Product updatedProduct, Long brandId ){
        Optional<Product> existingProductOptional = productRepository.findById(productId);
        if(existingProductOptional.isPresent()){
            Product existingProduct = existingProductOptional.get();
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setImg(updatedProduct.getImg());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setQuantity(updatedProduct.getQuantity());
            // kiem tra brand có tồn tại hay không
            Optional<Brand> optionalBrand = brandRepository.findById(brandId);
            if(optionalBrand.isPresent()){
                Brand brand = optionalBrand.get();
                existingProduct.setBrand(brand);
            }else {
                throw new BrandNotFoundException("Không tìm thấy thương hiệu với ID: " + brandId);
            }
           return productRepository.save(existingProduct);
        }else {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }
    }

//    public List<Product> getAllProductsByPriceAsc() {
//        return productRepository.findAllProductByPriceAsc();
//    }
//
//    public List<Product> getAllProductsByPriceDesc() {
//        return productRepository.findAllProductByPriceDesc();
//    }
}

