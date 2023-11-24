package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.Services.BestSellingProductsService;
import com.ecommerce.WatchStore.Services.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Product/best-selling-products")
public class BestSellingProductController {
    @Autowired
    private BestSellingProductsService bestSellingProductsService;
    @GetMapping
    public ResponseEntity<List<BestSellingProductDTO>> getBestSellingProducts() {
        List<BestSellingProductDTO> bestSellingProducts = bestSellingProductsService.getBestSellingProducts();
        return ResponseEntity.ok(bestSellingProducts);
    }
    @GetMapping("/top3")
    public ResponseEntity<List<BestSellingProductDTO>> getTop3SellingProducts(){
        List<BestSellingProductDTO> top3Products = bestSellingProductsService.getTop3SellingProuducts();
        return ResponseEntity.ok(top3Products);
    }

    @GetMapping("/price-range/")
    public ResponseEntity<List<BestSellingProductDTO>> getBestSellingProductsByPriceRange(@RequestParam double min , @RequestParam double max){
        List<BestSellingProductDTO> top3Products = bestSellingProductsService.getBestSellingProductsByPriceRange(min, max);
        return ResponseEntity.ok(top3Products);
    }


}
