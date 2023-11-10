package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.ProductRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.IOException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    private ProductRepository productRepository;

    @Value("${file.upload.directory}")
    private String uploadPath;
    public void exportToExcel(HttpServletResponse response) throws IOException {

        String excelFilePath = uploadPath + "/products.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Products");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên sản phẩm");
        headerRow.createCell(2).setCellValue("Giá");
        headerRow.createCell(3).setCellValue("Brand");
        headerRow.createCell(4).setCellValue("Category");
        headerRow.createCell(5).setCellValue("Active");
        headerRow.createCell(6).setCellValue("CreatedBy");
        headerRow.createCell(7).setCellValue("Quantity");
        headerRow.createCell(8).setCellValue("Accessory");
        headerRow.createCell(9).setCellValue("Img");
        headerRow.createCell(10).setCellValue("Thumbnail");
        headerRow.createCell(11).setCellValue("Gender");
        headerRow.createCell(12).setCellValue("Code");
        headerRow.createCell(13).setCellValue("Color");
        headerRow.createCell(14).setCellValue("Description");
        headerRow.createCell(15).setCellValue("Status");

        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        List<Product> products = productRepository.findAll();

        // Điền dữ liệu sản phẩm vào Excel
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getProductId());
            row.createCell(1).setCellValue(product.getProductName());
            row.createCell(2).setCellValue(product.getPrice());
            row.createCell(3).setCellValue(product.getBrand().getName());
            row.createCell(4).setCellValue(product.getCategory().getName());
            row.createCell(5).setCellValue(product.getActive());
            row.createCell(6).setCellValue(product.getCreatedBy());
            row.createCell(7).setCellValue(product.getQuantity());
            row.createCell(8).setCellValue(product.getAccessory().getName());
            row.createCell(9).setCellValue(product.getImg());
            row.createCell(10).setCellValue(product.getThumbnail());
            row.createCell(11).setCellValue(product.getGender());
            row.createCell(12).setCellValue(product.getCode());
            row.createCell(13).setCellValue(product.getColor());
            row.createCell(14).setCellValue(product.getDescription());
            row.createCell(15).setCellValue(product.getStatus());
        }

        for (int i = 0; i <= 15; i++) {
            sheet.autoSizeColumn(i);
        }
        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
//        OutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        outputStream.close();
//        workbook.close();
    }

    public void importFromExcel(MultipartFile file) throws IOException {
        // Đọc dữ liệu từ tệp Excel được tải lên
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);

        // Lấy trang tính đầu tiên (assumption: bạn chỉ có một trang tính)
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Bỏ qua hàng header
                continue;
            }

            // Đọc dữ liệu từ từng ô trong hàng và lưu vào cơ sở dữ liệu
            Long id = (long) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            float price = (float) row.getCell(2).getNumericCellValue();

            // Tạo và lưu sản phẩm vào cơ sở dữ liệu
            Product product = new Product();
            product.setProductId(id);
            product.setProductName(name);
            product.setPrice(price);

            productRepository.save(product);
        }

        workbook.close();
    }
}
