package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BestSellingProductDTO;
import com.ecommerce.WatchStore.DTO.BillDetailDTO;
import com.ecommerce.WatchStore.Entities.Bill;
import com.ecommerce.WatchStore.Entities.BillDetail;
import com.ecommerce.WatchStore.Entities.Brand;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Repositories.BillDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private BillService billService;

    public List<BillDetail> getBillDetail(){
        return billDetailRepository.findAll();
    }

    public BillDetail createBillDetail(BillDetail newBillDetail) {

        Product product = productService.getProductById( newBillDetail.getProduct().getProductId());
        Bill bill = billService.getBillById(newBillDetail.getBill().getId());

        BillDetail billDetail = new BillDetail();
        if (product == null || bill  == null) {
            // Nếu không có sản phẩm hoặc bill ID, hoặc chúng không hợp lệ, bạn có thể xử lý lỗi ở đây
            throw new IllegalArgumentException("Không tìm thấy sản phẩm hoặc id của hóa đơn phù hợp.");
        }else
        {
            float unitPrice = product.getPrice();
            int quantity = newBillDetail.getQuantity();
            float newTotalPrice = bill.getTotalPrice() + (unitPrice * quantity);
            bill.setTotalPrice(newTotalPrice);
            billService.updateBill(newBillDetail.getBill().getId(),bill);

            billDetail.setBill(bill);
            billDetail.setProduct(product);
            billDetail.setUnitPrice(unitPrice);
            billDetail.setQuantity( newBillDetail.getQuantity());
            billDetail.setCreatedBy(newBillDetail.getCreatedBy());
            billDetail.setActive(newBillDetail.getActive());

            return billDetailRepository.save(billDetail);
        }

    }



}
