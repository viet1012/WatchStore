package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.Entities.*;
import com.ecommerce.WatchStore.Repositories.BillDetailRepository;
import com.ecommerce.WatchStore.Repositories.BillRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;

    public Bill createBill(Bill bill, Long userId, Long voucherId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if( userOptional.isPresent())
        {
            User user = userOptional.get();

            Bill newBill = new Bill();
            newBill.setUser(user);
            newBill.setDeliverAddress(bill.getDeliverAddress());
            newBill.setTotalPrice(0);
            newBill.setCreatedBy(user.getDisplayName());
            newBill.setActive(true);
            newBill.setStatus("Đã đặt hàng");
            Bill savedBill = billRepository.save(newBill);


            return savedBill;
        }else {
            System.out.println("Không tìm thấy userID : "+ userId);
        }
        return null;

    }
    // Phương thức để cập nhật trạng thái của hóa đơn
    public Bill updateBillStatus(Long billId, String newStatus) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();

            // Cập nhật trạng thái mới cho hóa đơn
            bill.setStatus(newStatus);

            // Lưu hóa đơn đã được cập nhật vào cơ sở dữ liệu
            Bill updatedBill = billRepository.save(bill);

            return updatedBill;
        } else {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn với ID đã cung cấp: " + billId);
        }
    }

    public Bill getBillById(Long id ){
        Optional<Bill> billOptional = billRepository.findById(id);
        return billOptional.orElse(null);
    }

//    public List<BillDetail> getBillDetailsByBillId(Long billId) {
//        Bill bill = billRepository.findById(billId).orElse(null);
//        if (bill == null) {
//            throw new IllegalArgumentException("Không tìm thấy hóa đơn với id: " + billId);
//        }
//        return bill.getBillDetails();
//    }

    public List<Bill> getBillList(){
        return billRepository.findAll();
    }

    public Bill updateBill(Long id , Bill bill){
        Bill existingBill = billRepository.findById(id).orElseThrow(() ->  new IllegalArgumentException("Không tìm thấy hóa đơn với id: " + id));
        existingBill.setDeliverAddress(bill.getDeliverAddress());
        existingBill.setActive(true);
        existingBill.setUpdatedBy(bill.getUpdatedBy());
        existingBill.setUpdatedDate(bill.getUpdatedDate());

        return billRepository.save(existingBill);
    }
    public List<Bill> getBillsByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Bill> billPage = billRepository.findAll(pageable);
        return billPage.getContent(); // Lấy danh sách receipt trên trang cụ thể.
    }

    public long getTotalBills() {
        return billRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số receipt.
    }
}
