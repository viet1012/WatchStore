package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Entities.Voucher;
import com.ecommerce.WatchStore.Repositories.BillDetailRepository;
import com.ecommerce.WatchStore.Repositories.BillRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherService voucherService;


    public Bill createBill(BillDTO bill, Long userId, Long voucherId) {
        float newPrice = 0f;
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        Voucher voucher = voucherOptional.get();
        newPrice = voucherService.applyVoucherDiscount(bill , voucher);
        Optional<User> userOptional = userRepository.findById(userId);
        if( userOptional.isPresent())
        {
            User user = userOptional.get();

            Bill newBill = new Bill();
            newBill.setUser(user);
            newBill.setDeliverAddress(bill.getDeliverAddress());
            newBill.setTotalPrice(newPrice);
            newBill.setCreatedBy(user.getDisplayName());
            newBill.setActive(true);
            newBill.setCreatedDate(bill.getCreateDate());
            newBill.setVoucher(voucher);
            Bill savedBill = billRepository.save(newBill);

            // Lấy danh sách chi tiết đơn hàng tương ứng với đơn hàng mới lưu

            return savedBill;
        }else {
            System.out.println("Không tìm thấy userID : "+ userId);
        }
        return null;

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

}
