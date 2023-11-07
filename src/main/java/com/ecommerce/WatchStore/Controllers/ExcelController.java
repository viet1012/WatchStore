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

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        excelService.exportToExcel(response);
    }

    @PostMapping("/import")
    public void importFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        excelService.importFromExcel(file);
    }
}
