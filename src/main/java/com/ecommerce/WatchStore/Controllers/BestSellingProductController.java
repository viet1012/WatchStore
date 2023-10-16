package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.Services.BestSellingProductsService;
import com.ecommerce.WatchStore.Services.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/best-selling-products")
public class BestSellingProductController {
    @Autowired
    private BestSellingProductsService bestSellingProductsService;
    @GetMapping
    public ResponseEntity<List<BestSellingProductDTO>> getBestSellingProducts() {
        List<BestSellingProductDTO> bestSellingProducts = bestSellingProductsService.getBestSellingProducts();
        return ResponseEntity.ok(bestSellingProducts);
    }
}
