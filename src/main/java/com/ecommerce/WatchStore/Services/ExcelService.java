package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.ReceiptDTO;
import com.ecommerce.WatchStore.Entities.*;
import com.ecommerce.WatchStore.Repositories.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;

import org.apache.poi.ss.usermodel.*;

import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

@Service
public class ExcelService {
    @Autowired
    private UserRepository userRepository;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReceiptService receiptService;
    @Value("${file.upload.directory}")
    private String uploadPath;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);

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
        headerRow.createCell(5).setCellValue("Hoạt động");
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
            row.createCell(5).setCellValue(product.getActive() ? "Có" : "Không");
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
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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
        headerRow.createCell(10).setCellValue("Hoạt động");

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
            row.createCell(10).setCellValue(bill.getActive() ? "Có" : "Không");

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
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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
        headerRow.createCell(9).setCellValue("Hoạt động");


        // Lấy danh sách chi tiết hóa đơn từ cơ sở dữ liệu
        List<BillDetail> billDetails = billDetailRepository.findAll();

        // Điền dữ liệu chi tiết hóa đơn vào Excel
        int rowNum = 1;
        for (BillDetail billDetail : billDetails) {

            if (billDetail.getBill() != null) {
                System.out.println(billDetail.getBill().getId());

            }

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(billDetail.getId());
            row.createCell(1).setCellValue(billDetail.getBill() != null ? billDetail.getBill().getId().toString() : null);
            row.createCell(2).setCellValue(billDetail.getProduct().getProductId());
            row.createCell(3).setCellValue(billDetail.getUnitPrice());
            row.createCell(4).setCellValue(billDetail.getQuantity());
            row.createCell(5).setCellValue(billDetail.getCreatedBy());
            row.createCell(6).setCellValue(billDetail.getCreatedDate().toString());
            row.createCell(7).setCellValue(billDetail.getUpdatedBy());
            row.createCell(8).setCellValue(billDetail.getUpdatedDate().toString());
            row.createCell(9).setCellValue(billDetail.getActive() ? "Có" : "Không");
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
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
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
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

    }

    public void exportToExcelCategory(HttpServletResponse response) throws IOException {
        String excelFilePath = uploadPath + "/categories.xlsx";
        File excelFile = new File(excelFilePath);

        // Tạo một Workbook Excel mới
        Workbook workbook = new XSSFWorkbook();

        // Tạo một trang tính mới
        Sheet sheet = workbook.createSheet("Categories");

        // Tạo hàng đầu tiên (header) trong Excel
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Tên");
        headerRow.createCell(2).setCellValue("Người tạo");
        headerRow.createCell(3).setCellValue("Ngày tạo");
        headerRow.createCell(4).setCellValue("Người cập nhật");
        headerRow.createCell(5).setCellValue("Ngày cập nhật");
        headerRow.createCell(6).setCellValue("Hoạt động");

        // Lấy danh sách các category từ cơ sở dữ liệu
        List<Category> categories = categoryRepository.findAll();

        // Điền dữ liệu của category vào Excel
        int rowNum = 1;
        for (Category category : categories) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(category.getId());
            row.createCell(1).setCellValue(category.getName());
            row.createCell(2).setCellValue(category.getCreatedBy());
            row.createCell(3).setCellValue(category.getCreatedDt().toString());
            row.createCell(4).setCellValue(category.getUpdatedBy());
            row.createCell(5).setCellValue(category.getUpdatedDt().toString());
            row.createCell(6).setCellValue(category.isActive() ? "Có" : "Không");
        }

        for (int i = 0; i <= 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập loại Content-Type và header cho phản hồi
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=categories.xlsx");

        // Ghi dữ liệu từ Workbook vào HttpServletResponse
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(fileOutputStream);
        }
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    public void importFromExcelSupplier(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();

        // Bỏ qua hàng tiêu đề
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Đọc dữ liệu từ mỗi hàng và tạo đối tượng Supplier
            Supplier supplier = new Supplier();
            supplier.setId(row.getCell(0) != null ? (long) row.getCell(0).getNumericCellValue() : null);
            supplier.setName(row.getCell(1).getStringCellValue());
            supplier.setAddress(row.getCell(2).getStringCellValue());
            supplier.setPhoneNumber(row.getCell(3).getStringCellValue());
            supplier.setEmail(row.getCell(4).getStringCellValue());
            supplier.setCreatedBy(row.getCell(5).getStringCellValue());
            supplier.setCreatedDate(LocalDateTime.parse(row.getCell(6).getStringCellValue()));
            supplier.setUpdatedBy(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : null);
            supplier.setUpdatedDate(LocalDateTime.parse(row.getCell(8).getStringCellValue()));
            if (row.getCell(9).getStringCellValue().equalsIgnoreCase("Có")) {
                supplier.setActive(true);
            } else {
                supplier.setActive(false);
            }
            Supplier existingNameSupplier = supplierRepository.findByName(supplier.getName());
            Optional<Supplier> existingSupplierId = supplierRepository.findById(supplier.getId());

            if (existingNameSupplier == null && existingSupplierId.isEmpty()) {
                // Lưu Supplier vào cơ sở dữ liệu nếu không tồn tại
                supplierRepository.save(supplier);
            } else {

                LOGGER.error("Nhà cung cấp đã tồn tại: " + supplier.getName());
            }
            // Lưu Supplier vào cơ sở dữ liệu thông qua repository

        }
        workbook.close();
    }

    public void importFromExcelReceipt(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Bỏ qua hàng header
                continue;
            }

            Receipt receipt = new Receipt();

            // Đọc dữ liệu từ từng ô trong hàng và gán cho các trường tương ứng của Receipt
            receipt.setId((long) row.getCell(0).getNumericCellValue());
            String supplierName = row.getCell(1).getStringCellValue();
            Supplier supplier = supplierRepository.findByName(supplierName);
            receipt.setSupplier(supplier);
            String username = row.getCell(2).getStringCellValue();
            User user = userRepository.findByDisplayName(username);
            receipt.setUser(user);
            receipt.setTotal((float) row.getCell(3).getNumericCellValue());
            receipt.setCreatedBy(row.getCell(4).getStringCellValue());
            Date createdDate = row.getCell(5).getDateCellValue();
            receipt.setCreatedDate(createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            receipt.setUpdatedBy(row.getCell(6).getStringCellValue());
            Date updatedDate = row.getCell(7).getDateCellValue();
            receipt.setUpdatedDate(updatedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            String activeValue = row.getCell(8).getStringCellValue();
            receipt.setActive(activeValue.equalsIgnoreCase("Có"));

            receiptRepository.save(receipt);
        }

        workbook.close();
    }

    public void importFromExcelProduct(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                // Bỏ qua hàng header
                continue;
            }

            Product product = new Product();
            product.setProductId((long) row.getCell(0).getNumericCellValue());
            product.setProductName(row.getCell(1).getStringCellValue());
            product.setPrice((float) row.getCell(2).getNumericCellValue());
            product.setQuantity((int) row.getCell(3).getNumericCellValue());

            // Đoạn code sau sẽ lấy thông tin Brand từ cột 4 của file Excel
            Brand brand = new Brand();
            brand.setName(row.getCell(4).getStringCellValue());
            product.setBrand(brand);

            // Đoạn code sau sẽ lấy thông tin Category từ cột 5 của file Excel
            Category category = new Category();
            category.setName(row.getCell(5).getStringCellValue());
            product.setCategory(category);

            product.setCreatedBy(row.getCell(6).getStringCellValue());

            // Lấy thông tin thời gian tạo
            Date createdDateTime = row.getCell(7).getDateCellValue();
            product.setCreatedDate(createdDateTime);

            product.setUpdatedBy(row.getCell(8).getStringCellValue());

            // Lấy thông tin thời gian cập nhật
            Date updatedDateTime = row.getCell(9).getDateCellValue();
            product.setUpdatedDate(updatedDateTime);

            // Xác định trạng thái Active hoặc Inactive từ cột 10
            String active = row.getCell(10).getStringCellValue();
            product.setActive(active.equalsIgnoreCase("Có"));

            product.setCode(row.getCell(11).getStringCellValue());
            product.setThumbnail(row.getCell(12).getStringCellValue());
            product.setGender(row.getCell(13).getStringCellValue());
            product.setStatus(row.getCell(14).getStringCellValue());
            product.setColor(row.getCell(15).getStringCellValue());
            product.setDescription(row.getCell(16).getStringCellValue());

            // Lưu đối tượng Product vào cơ sở dữ liệu
            productRepository.save(product);
        }

        workbook.close();
    }


    public void exportToPdf(HttpServletResponse response, Long receiptId) {
        try {
            // Tạo một document mới
            Document document = new Document(PageSize.A4);

            // Tạo một ByteArrayOutputStream để lưu dữ liệu PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Tạo PdfWriter để ghi dữ liệu vào document và ByteArrayOutputStream
            // PdfWriter writer = PdfWriter.getInstance(document, baos);
            String pdfFilePath = uploadPath + "/receipts.pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));

            // Thiết lập font chữ và màu sắc
            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.LIGHT_GRAY);
            // Mở document để bắt đầu ghi dữ liệu
            document.open();

            Paragraph title = new Paragraph("Receipts Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);

            // Lấy danh sách các receipt từ cơ sở dữ liệu
          //  List<Receipt> receipts = receiptRepository.findAll();

            Optional<Receipt> optionalReceipt = receiptRepository.findById(receiptId);
            Receipt receipt = optionalReceipt.get();
            // Viết dữ liệu của mỗi receipt vào document

//            for (Receipt receipt : receipts) {
                Paragraph paragraph = new Paragraph();

                // Thêm các thông tin receipt vào document với font tương ứng
                paragraph.add(new Chunk("ID: ", boldFont));
                paragraph.add(new Chunk(receipt.getId() + "\n", font));

                paragraph.add(new Chunk("Supplier: ", boldFont));
                paragraph.add(new Chunk(receipt.getSupplier().getName() + "\n", font));

                paragraph.add(new Chunk("User: ", boldFont));
                paragraph.add(new Chunk(receipt.getUser().getDisplayName() + "\n", font));

                paragraph.add(new Chunk("Total: ", boldFont));
                paragraph.add(new Chunk(String.valueOf(receipt.getTotal()) + "\n", font));

                paragraph.add(new Chunk("Created By: ", boldFont));
                paragraph.add(new Chunk(receipt.getCreatedBy() + "\n", font));

                paragraph.add(new Chunk("Created Date: ", boldFont));
                paragraph.add(new Chunk(receipt.getCreatedDate().toString() + "\n", font));

                paragraph.add(new Chunk("Updated By: ", boldFont));
                paragraph.add(new Chunk(receipt.getUpdatedBy() + "\n", font));

                paragraph.add(new Chunk("Updated Date: ", boldFont));
                paragraph.add(new Chunk(receipt.getUpdatedDate().toString() + "\n", font));

                paragraph.add(new Chunk("Active: ", boldFont));
                paragraph.add(new Chunk((receipt.isActive() ? "Yes" : "No") + "\n", font));

                document.add(paragraph);
                document.add(Chunk.NEWLINE);
            //}

            // Đóng document
            document.close();

            // Thiết lập loại Content-Type và header cho phản hồi
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=receipts.pdf");


            // Gửi dữ liệu PDF về phản hồi HTTP
            OutputStream outputStream = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(pdfFilePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            fileInputStream.close();
            outputStream.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }


}
