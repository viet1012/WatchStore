package com.ecommerce.WatchStore.Controllers;


import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.Receipt;
import com.ecommerce.WatchStore.Entities.Voucher;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/Admin/Vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;


    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<Voucher>>> listVoucher() {
        List<Voucher> vouchers = voucherService.getListVoucher();
        Long totalVouchers = voucherService.total();
        ResponseWrapper<List<Voucher>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Successfully", true, totalVouchers, vouchers);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/Create")
    public Voucher createVoucher(@RequestBody Voucher voucher) {
        return voucherService.createVoucher(voucher);
    }

    @PutMapping("/Update")
    public Voucher updateVoucher(@RequestBody Voucher voucher, @RequestParam Long voucherId) {
        return voucherService.updateVoucher(voucher, voucherId);
    }

    @DeleteMapping("/Delete")
    public void deleteVoucher(@RequestParam Long voucherId) {
        voucherService.deleteVoucher(voucherId);
    }
}
