package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.ProductSalesDTO;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Services.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/Admin/sales")
public class SalesController {

    @Autowired
    private SaleService saleService;

    @GetMapping("/GetAll")
    public ResponseEntity<List<ProductSalesDTO>> getProductSales() {
        List<ProductSalesDTO> productSales = saleService.getProductSales();
        return ResponseEntity.ok(productSales);
    }
    @GetMapping("/GetTopBuyer")
    public ResponseEntity<ProductSalesDTO> getTopBuyer() {
        ProductSalesDTO topBuyer = saleService.getUserWithMostPurchases();
        if (topBuyer != null) {
            return ResponseEntity.ok(topBuyer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/by-date-range")
    public ResponseEntity<ProductSalesDTO> getBestSellingProductByDateRange(
            @RequestParam("startDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        ProductSalesDTO bestSellingProduct = saleService.getBestSellingProductByDateRange(startDateTime, endDateTime);

        if (bestSellingProduct != null) {
            return ResponseEntity.ok(bestSellingProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
