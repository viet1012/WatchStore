package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.Voucher;
import com.ecommerce.WatchStore.Repositories.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    public boolean isVoucherValid(Voucher voucher) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(voucher.getStartDate()) && now.isBefore(voucher.getEndDate());
    }

    public float applyVoucherDiscount(BillDTO bill, Voucher voucher) {
        if (isVoucherValid(voucher)) {
            float discountPercentage = voucher.getValue() / 100.0f; // Chuyển phần trăm thành tỷ lệ
            float discountAmount = bill.getTotalPrice() * discountPercentage;

            // Giảm giá chỉ khi discountAmount không vượt quá tổng hóa đơn
            if (discountAmount <= bill.getTotalPrice()) {
                float newTotalPrice = bill.getTotalPrice() - discountAmount;
                bill.setTotalPrice(newTotalPrice);
                return discountAmount;
            }
        }
        return 0.0f; // Không áp dụng giảm giá nếu voucher không hợp lệ hoặc giảm giá vượt quá tổng hóa đơn.
    }

    public List<Voucher> getListVoucher() {
        return voucherRepository.findAll();
    }

    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Voucher voucher, Long voucherId) {
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if(voucherOptional.isPresent()){
            Voucher updatedVoucher = voucherOptional.get();
            updatedVoucher.setCode(voucher.getCode());
            updatedVoucher.setValue(voucher.getValue());
            updatedVoucher.setStartDate(voucher.getStartDate());
            updatedVoucher.setEndDate(voucher.getEndDate());
            updatedVoucher.setUpdatedDate(voucher.getUpdatedDate());
            return voucherRepository.save(updatedVoucher);
        }
        return null;

    }

    public void deleteVoucher(Long voucherId) {
         voucherRepository.deleteById(voucherId);
    }
}
