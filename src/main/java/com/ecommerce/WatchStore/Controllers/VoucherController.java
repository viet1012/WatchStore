package com.ecommerce.WatchStore.Controllers;


import com.ecommerce.WatchStore.Entities.Voucher;
import com.ecommerce.WatchStore.Services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;
    @GetMapping("/list")
    public ResponseEntity<List<Voucher>> listVoucher() {
        List<Voucher> vouchers = voucherService.getListVoucher();
        return ResponseEntity.ok(vouchers);
    }
    @PostMapping("/create")
    public Voucher createVoucher(@RequestBody Voucher voucher) {
        return voucherService.createVoucher(voucher);
    }
    @PutMapping("/update")
    public Voucher updateVoucher(@RequestBody Voucher voucher, @RequestParam Long voucherId) {
        return voucherService.updateVoucher(voucher, voucherId);
    }
    @DeleteMapping("/delete")
    public void deleteVoucher(@RequestParam Long voucherId) {
         voucherService.deleteVoucher(voucherId);
    }
}
