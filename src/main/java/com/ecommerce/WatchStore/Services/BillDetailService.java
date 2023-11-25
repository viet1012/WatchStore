package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.DTO.BillDetailDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private BillService billService;
    @Autowired
    private EmailService emailService;

    public List<BillDetail> getBillDetail(){
        return billDetailRepository.findAll();
    }
    private float calculateTotalPrice(float unitPrice, int quantity) {
        return unitPrice * quantity;
    }
    public BillDetail createBillDetail(BillDetail newBillDetail, Long billId, Long productId) {

        Product product = productService.getProductById( productId);
        Bill bill = billService.getBillById(billId);

        BillDetail billDetail = new BillDetail();
        if(product.getActive() == false){
            throw new IllegalArgumentException("Không tìm thấy sản phẩm!");
        }
        if (product == null || bill  == null) {
            // Nếu không có sản phẩm hoặc bill ID, hoặc chúng không hợp lệ, bạn có thể xử lý lỗi ở đây
            throw new IllegalArgumentException("Không tìm thấy sản phẩm hoặc id của hóa đơn phù hợp.");
        }else
        {
            float unitPrice = product.getPrice();
            int quantity = newBillDetail.getQuantity();
            float newTotalPrice = bill.getTotalPrice() + (unitPrice * quantity);
            bill.setTotalPrice(newTotalPrice);
            billService.updateBill(billId,bill);

            billDetail.setBill(bill);
            billDetail.setProduct(product);

            billDetail.setUnitPrice(unitPrice);
            billDetail.setQuantity( newBillDetail.getQuantity());
            billDetail.setCreatedBy(bill.getUser().getDisplayName());
            billDetail.setActive(true);

            BillDetail billDetail1 = billDetailRepository.save(billDetail);

            List<BillDetail> billDetails = billDetailRepository.findByBill(bill);
            // Gửi email thông báo với danh sách chi tiết đơn hàng
            emailService.sendOrderConfirmationEmail(bill.getUser().getEmail(), bill, billDetails);

            return  billDetail1;
            // Lấy danh sách chi tiết đơn hàng tương ứng với đơn hàng mới lưu

        }

    }

    public BillDetail updateBillDetail(Long billDetailId, BillDetail updatedBillDetail) {
        Optional<BillDetail> existingBillDetailOptional = billDetailRepository.findById(billDetailId);

        if (existingBillDetailOptional.isPresent()) {
            BillDetail existingBillDetail = existingBillDetailOptional.get();
            existingBillDetail.setQuantity(updatedBillDetail.getQuantity());

            // Cập nhật thông tin khác nếu cần

            return billDetailRepository.save(existingBillDetail);
        } else {
            throw new IllegalArgumentException("Không tìm thấy chi tiết hóa đơn với ID: " + billDetailId);
        }
    }
    public List<BillDetail> getBillDetailsByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<BillDetail> billPage = billDetailRepository.findAll(pageable);
        return billPage.getContent(); // Lấy danh sách receipt trên trang cụ thể.
    }

    public long getTotalBillDetails() {
        return billDetailRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số receipt.
    }
}
