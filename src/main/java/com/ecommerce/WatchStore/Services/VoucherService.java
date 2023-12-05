package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.Voucher;
import com.ecommerce.WatchStore.Repositories.BillRepository;
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
    @Autowired
    private BillRepository billRepository;

    public boolean isVoucherValid(Voucher voucher) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(voucher.getStartDate()) && now.isBefore(voucher.getEndDate());
    }

    public Voucher getVoucherFromId(Long voucherId) {
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (voucherOptional.isEmpty()) {
            throw new IllegalArgumentException("Voucher không hợp lệ.");
        } else {
            return voucherOptional.get();
        }
    }

    public void applyVoucherDiscount(Long billId, Long voucherId) {
        Optional<Bill> billOptional = billRepository.findById(billId);
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);


        if (voucherOptional.isEmpty()) {
            throw new IllegalArgumentException("Voucher không hợp lệ.");
        }
        Voucher voucher = voucherOptional.get();
        if (billOptional.isPresent()) {
            Bill bill = billOptional.get();

            System.out.println("Bill :" + bill.getTotalPrice() + " Voucher: " + voucher.getValue());
            if (bill.getTotalPrice() < 0) {
                throw new IllegalArgumentException("Tổng tiền hóa đơn không phù hợp điều kiện áp dụng voucher.");
            }

            if (isVoucherValid(voucher)) {
                // float discountPercentage = voucher.getValue() / 100.0f; // Chuyển phần trăm thành tỷ lệ
                // float discountAmount = bill.getTotalPrice() * discountPercentage;

                float discountAmount = voucher.getValue();
                // Giảm giá chỉ khi discountAmount không vượt quá tổng hóa đơn
                if (discountAmount <= bill.getTotalPrice()) {
                    float newTotalPrice = bill.getTotalPrice() - discountAmount;
                    bill.setTotalPrice(newTotalPrice);
                    billRepository.save(bill);
                } else {
                    throw new IllegalArgumentException("Số tiền giảm giá vượt quá tổng giá trị hóa đơn.");
                }
            } else {
                throw new IllegalArgumentException("Voucher đã quá hạn sử dụng.");
            }
        } else {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn với ID: " + billId);
        }
    }

    public void applyVoucher(Bill bill, Long voucherId) {
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);


        if (voucherOptional.isEmpty()) {
            throw new IllegalArgumentException("Voucher không hợp lệ.");
        }else{
            Voucher voucher = voucherOptional.get();

            if (bill.getTotalPrice() < 0) {
                throw new IllegalArgumentException("Tổng tiền hóa đơn không phù hợp điều kiện áp dụng voucher.");
            }

            if (isVoucherValid(voucher)) {

                float discountAmount = voucher.getValue();
                // Giảm giá chỉ khi discountAmount không vượt quá tổng hóa đơn
                if (discountAmount <= bill.getTotalPrice()) {
                    float newTotalPrice = bill.getTotalPrice() - discountAmount;
                    bill.setVoucher(voucher);
                    bill.setTotalPrice(newTotalPrice);

                } else {
                    throw new IllegalArgumentException("Số tiền giảm giá vượt quá tổng giá trị hóa đơn.");
                }
            }
            else {
                throw new IllegalArgumentException("Voucher đã hết hạn sử dụng.");

            }
        }

    }

    public List<Voucher> getListVoucher() {
        return voucherRepository.findAll();
    }

    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Voucher voucher, Long voucherId) {
        Optional<Voucher> voucherOptional = voucherRepository.findById(voucherId);
        if (voucherOptional.isPresent()) {
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
