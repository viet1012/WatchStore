package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.Services.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @GetMapping("/export-product")
    public void exportToExcelProduct(HttpServletResponse response) throws IOException {
        excelService.exportToExcelProduct(response);
    }

    @GetMapping("/export-bill")
    public void exportToExcelBill(HttpServletResponse response) throws IOException {
        excelService.exportToExcelBill(response);
    }

    @GetMapping("/export-bill-details")
    public void exportToExcelBillDetail(HttpServletResponse response) throws IOException {
        excelService.exportToExcelBillDetail(response);
    }

    @GetMapping("/export-brand")
    public void exportToExcelBrand(HttpServletResponse response) throws IOException {
        excelService.exportToExcelBrand(response);
    }

    @GetMapping("/export-supplier")
    public void exportToExcelSupplier(HttpServletResponse response) throws IOException {
        excelService.exportToExcelSupplier(response);
    }

    @GetMapping("/export-receipt")
    public void exportToExcelReceipt(HttpServletResponse response) throws IOException {
        excelService.exportToExcelReceipt(response);
    }

    @PostMapping("/import")
    public void importFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importFromExcel(file);
    }
}
