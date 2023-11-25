package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.ProductSalesDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.BillService;
import com.ecommerce.WatchStore.Services.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/Admin/Sales")
public class SalesController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private BillService billService;

    @GetMapping("/Bill/GetAll")
    public ResponseEntity<ResponseWrapper<List<Bill>>> getBillList() {
        List<Bill> billList = billService.getBillList();
        long totalBills = billService.getTotalBills();

        if (billList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ResponseWrapper<List<Bill>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalBills, billList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<ProductSalesDTO>>> getProductSales() {
        List<ProductSalesDTO> productSales = saleService.getProductSales();
        long totalProductSales = saleService.getTotalProductSales();
        ResponseWrapper<List<ProductSalesDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, totalProductSales, productSales);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/GetTopBuyer")
//    public ResponseEntity<ResponseWrapper<ProductSalesDTO>> getTopBuyer() {
//        ProductSalesDTO topBuyer = saleService.getUserWithMostPurchases();
//        ResponseWrapper<ProductSalesDTO> response = (topBuyer != null) ?
//                new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, topBuyer) :
//                new ResponseWrapper<>(HttpStatus.NOT_FOUND.value(), "Top buyer not found", false, null);
//
//        return (topBuyer != null) ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
//    }

    @GetMapping("/by-date-range")
    public ResponseEntity<ResponseWrapper<ProductSalesDTO>> getBestSellingProductByDateRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate
    ) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        ProductSalesDTO bestSellingProduct = saleService.getBestSellingProductByDateRange(startDateTime, endDateTime);
        ResponseWrapper<ProductSalesDTO> response = (bestSellingProduct != null) ?
                new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true, bestSellingProduct) :
                new ResponseWrapper<>(HttpStatus.NOT_FOUND.value(), "Best selling product not found", false, null);

        return (bestSellingProduct != null) ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}
