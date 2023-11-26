package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.Entities.*;
import com.ecommerce.WatchStore.Repositories.*;
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
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Value("${file.upload.directory}")
    private String uploadPath;

    public void exportToExcelProduct(HttpServletResponse response) throws IOException {

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
        headerRow.createCell(3).setCellValue("Thương hiệu");
        headerRow.createCell(4).setCellValue("Danh mục");
        headerRow.createCell(5).setCellValue("Trạng thái");
        headerRow.createCell(6).setCellValue("Tạo bởi");
        headerRow.createCell(7).setCellValue("Số lượng");
        headerRow.createCell(8).setCellValue("Phụ kiện");
        headerRow.createCell(9).setCellValue("Giới tính");
        headerRow.createCell(10).setCellValue("Mã");
        headerRow.createCell(11).setCellValue("Màu sắc");
        headerRow.createCell(12).setCellValue("Mô tả");
        headerRow.createCell(13).setCellValue("Trạng thái");
        headerRow.createCell(14).setCellValue("Ảnh");
        headerRow.createCell(15).setCellValue("Hình thu nhỏ");

        // Lấy danh sách sản phẩm từ cơ sở dữ liệu
        List<Product> products = productRepository.findAll();

        // Điền dữ liệu sản phẩm vào Excel
        int rowNum = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getProductId());
            row.createCell(1).setCellValue(product.getProductName());
            row.createCell(2).setCellValue(product.getPrice());
            row.createCell(3).setCellValue(product.getBrand() != null ? product.getBrand().getName() : "");
            row.createCell(4).setCellValue(product.getCategory() != null ? product.getCategory().getName() : "");
            row.createCell(5).setCellValue(product.getActive());
            row.createCell(6).setCellValue(product.getCreatedBy());
            row.createCell(7).setCellValue(product.getQuantity());
            row.createCell(8).setCellValue(product.getAccessory() != null ? product.getAccessory().getName() : "");
            row.createCell(9).setCellValue(product.getGender());
            row.createCell(10).setCellValue(product.getCode());
            row.createCell(11).setCellValue(product.getColor());
            row.createCell(12).setCellValue(product.getDescription());
            row.createCell(13).setCellValue(product.getStatus());
            row.createCell(14).setCellValue(product.getImg());
            row.createCell(15).setCellValue(product.getThumbnail());

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

    public void exportToExcelBill(HttpServletResponse response) throws IOException {
        String excelFilePath = uploadPath + "/bills.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Bill");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Địa chỉ giao hàng");
        headerRow.createCell(2).setCellValue("Tổng giá ");
        headerRow.createCell(3).setCellValue("Trạng thái");
        headerRow.createCell(4).setCellValue("Người dùng");
        headerRow.createCell(5).setCellValue("Khuyến mãi");
        headerRow.createCell(6).setCellValue("Tạo bởi");
        headerRow.createCell(7).setCellValue("Ngày tạo");
        headerRow.createCell(8).setCellValue("Cập nhật bởi");
        headerRow.createCell(9).setCellValue("Ngày cập nhật");
        headerRow.createCell(10).setCellValue("Trang thái");

        // Lấy danh sách hóa đơn từ cơ sở dữ liệu
        List<Bill> bills = billRepository.findAll();

        // Điền dữ liệu hóa đơn vào Excel
        int rowNum = 1;
        for (Bill bill : bills) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(bill.getId());
            row.createCell(1).setCellValue(bill.getDeliverAddress());
            row.createCell(2).setCellValue(bill.getTotalPrice());
            row.createCell(3).setCellValue(bill.getStatus());
            row.createCell(4).setCellValue(bill.getUser() != null ? bill.getUser().getDisplayName() : "");
            row.createCell(5).setCellValue(bill.getVoucher() != null ? bill.getVoucher().getCode() : "");
            row.createCell(6).setCellValue(bill.getCreatedBy());
            row.createCell(7).setCellValue(bill.getCreatedDate().toString());
            row.createCell(8).setCellValue(bill.getUpdatedBy());
            row.createCell(9).setCellValue(bill.getUpdatedDate().toString());
            row.createCell(10).setCellValue(bill.getActive());
        }

        for (int i = 0; i <= 10; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bills.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }
    }

    public void exportToExcelBillDetail(HttpServletResponse response) throws IOException {
        String excelFilePath = uploadPath + "/bill_details.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Bill Details");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("ID Hóa đơn");
        headerRow.createCell(2).setCellValue("ID Sản phẩm");
        headerRow.createCell(3).setCellValue("Đơn giá");
        headerRow.createCell(4).setCellValue("Số lượng");
        headerRow.createCell(5).setCellValue("Được tạo bởi");
        headerRow.createCell(6).setCellValue("Ngày tạo");
        headerRow.createCell(7).setCellValue("Được cập nhật bởi");
        headerRow.createCell(8).setCellValue("Ngày cập nhật");
        headerRow.createCell(9).setCellValue("Trang thái");


        // Lấy danh sách chi tiết hóa đơn từ cơ sở dữ liệu
        List<BillDetail> billDetails = billDetailRepository.findAll();

        // Điền dữ liệu chi tiết hóa đơn vào Excel
        int rowNum = 1;
        for (BillDetail billDetail : billDetails) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(billDetail.getId());
            row.createCell(1).setCellValue(billDetail.getBill().getId());
            row.createCell(2).setCellValue(billDetail.getProduct().getProductId());
            row.createCell(3).setCellValue(billDetail.getUnitPrice());
            row.createCell(4).setCellValue(billDetail.getQuantity());
            row.createCell(5).setCellValue(billDetail.getCreatedBy());
            row.createCell(6).setCellValue(billDetail.getCreatedDate().toString());
            row.createCell(7).setCellValue(billDetail.getUpdatedBy());
            row.createCell(8).setCellValue(billDetail.getUpdatedDate().toString());
            row.createCell(9).setCellValue(billDetail.getActive());
        }

        for (int i = 0; i <= 9; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=bill_details.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }
    }

    public void exportToExcelBrand(HttpServletResponse response) throws IOException {
        String excelFilePath = uploadPath + "/brands.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Brands");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên thương hiệu");
        headerRow.createCell(2).setCellValue("Ngày tạo");
        headerRow.createCell(3).setCellValue("Người tạo");
        headerRow.createCell(4).setCellValue("Người cập nhật");
        headerRow.createCell(5).setCellValue("Ngày cập nhật");
        headerRow.createCell(6).setCellValue("Hoạt động");


        // Lấy danh sách chi tiết hóa đơn từ cơ sở dữ liệu
        List<Brand> brands = brandRepository.findAll();

        // Điền dữ liệu chi tiết hóa đơn vào Excel
        int rowNum = 1;
        for (Brand brand : brands) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(brand.getIdBrand());
            row.createCell(1).setCellValue(brand.getName());
            row.createCell(2).setCellValue(brand.getCreatedDt().toString());
            row.createCell(3).setCellValue(brand.getCreatedBy());
            row.createCell(4).setCellValue(brand.getUpdatedBy());
            row.createCell(5).setCellValue(brand.getUpdatedDt().toString());
            row.createCell(6).setCellValue(brand.isActive() ? "Có" : "Không");
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=brands.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }
    }

    public void exportToExcelSupplier(HttpServletResponse response) throws IOException {
        String excelFilePath = uploadPath + "/suppliers.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Suppliers");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên nhà cung cấp");
        headerRow.createCell(2).setCellValue("Địa chỉ");
        headerRow.createCell(3).setCellValue("Số điện thoại");
        headerRow.createCell(4).setCellValue("Email");
        headerRow.createCell(5).setCellValue("Người tạo");
        headerRow.createCell(6).setCellValue("Ngày tạo");
        headerRow.createCell(7).setCellValue("Người cập nhật");
        headerRow.createCell(8).setCellValue("Ngày cập nhật");
        headerRow.createCell(9).setCellValue("Hoạt động");

        // Lấy danh sách nhà cung cấp từ cơ sở dữ liệu
        List<Supplier> suppliers = supplierRepository.findAll();

        // Điền dữ liệu nhà cung cấp vào Excel
        int rowNum = 1;
        for (Supplier supplier : suppliers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(supplier.getId());
            row.createCell(1).setCellValue(supplier.getName());
            row.createCell(2).setCellValue(supplier.getAddress());
            row.createCell(3).setCellValue(supplier.getPhoneNumber());
            row.createCell(4).setCellValue(supplier.getEmail());
            row.createCell(5).setCellValue(supplier.getCreatedBy());
            row.createCell(6).setCellValue(supplier.getCreatedDate().toString());
            row.createCell(7).setCellValue(supplier.getUpdatedBy());
            row.createCell(8).setCellValue(supplier.getUpdatedDate().toString());
            row.createCell(9).setCellValue(supplier.isActive() ? "Có" : "Không");
        }

        for (int i = 0; i <= 9; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=suppliers.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }
    }

    public void exportToExcelReceipt(HttpServletResponse response) throws IOException {
        String excelFilePath = uploadPath + "/receipts.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Receipts");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nhà cung cấp");
        headerRow.createCell(2).setCellValue("Người dùng");
        headerRow.createCell(3).setCellValue("Tổng");
        headerRow.createCell(4).setCellValue("Người tạo");
        headerRow.createCell(5).setCellValue("Ngày tạo");
        headerRow.createCell(6).setCellValue("Người cập nhật");
        headerRow.createCell(7).setCellValue("Ngày cập nhật");
        headerRow.createCell(8).setCellValue("Hoạt động");

        // Lấy danh sách biên lai từ cơ sở dữ liệu
        List<Receipt> receipts = receiptRepository.findAll();

        // Điền dữ liệu biên lai vào Excel
        int rowNum = 1;
        for (Receipt receipt : receipts) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(receipt.getId());
            row.createCell(1).setCellValue(receipt.getSupplier() != null ? receipt.getSupplier().getName() : "");
            row.createCell(2).setCellValue(receipt.getUser() != null ? receipt.getUser().getDisplayName() : "");
            row.createCell(3).setCellValue(receipt.getTotal());
            row.createCell(4).setCellValue(receipt.getCreatedBy());
            row.createCell(5).setCellValue(receipt.getCreatedDate().toString());
            row.createCell(6).setCellValue(receipt.getUpdatedBy());
            row.createCell(7).setCellValue(receipt.getUpdatedDate().toString());
            row.createCell(8).setCellValue(receipt.isActive() ? "Có" : "Không");
        }

        for (int i = 0; i <= 8; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=receipts.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }
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
