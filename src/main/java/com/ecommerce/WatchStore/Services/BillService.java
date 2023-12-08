package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.BillDTO;
import com.ecommerce.WatchStore.DTO.BillDetailDTO;
import com.ecommerce.WatchStore.Entities.*;
import com.ecommerce.WatchStore.Repositories.BillDetailRepository;
import com.ecommerce.WatchStore.Repositories.BillRepository;
import com.ecommerce.WatchStore.Repositories.UserRepository;
import com.ecommerce.WatchStore.Repositories.VoucherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;


    public List<BillDTO> getAll() {
        List<Object[]> billDetails = billRepository.getAllBillDetails();
        List<BillDTO> billDTOList = new ArrayList<>();

        for (Object[] billDetail : billDetails) {
            BillDTO billDTO = new BillDTO();

            // Mapping data from the query result to the BillDTO fields
            billDTO.setId((Long) billDetail[0]); // Assuming id is at index 0 in the query result
            billDTO.setTotalPrice((float) billDetail[1]); // Assuming totalPrice is at index 1 in the query result
            billDTO.setUserId((Long) billDetail[2]); // Assuming user_id is at index 2 in the query result
            billDTO.setDeliverAddress((String) billDetail[3]); // Assuming deliverAddress is at index 3 in the query result

            // Extract receipt details similarly to the getAllReceiptDetailsWithTotalAndSupplierId method
            List<BillDetail> billDetailList = new ArrayList<>();
            if (billDetail[4] instanceof BillDetail) {
                BillDetail singleBillDetail = (BillDetail) billDetail[4];
                billDetailList.add(singleBillDetail);
            } else if (billDetail[4] instanceof List<?>) {
                // If index 4 returns a list of BillDetail
                billDetailList = (List<BillDetail>) billDetail[4];
            }
            billDTO.setVoucherId((Long) billDetail[5]);
            List<BillDetailDTO> billDetailDTOs = billDetailList.stream()
                    .map(billDetailItem -> modelMapper.map(billDetailItem, BillDetailDTO.class))
                    .collect(Collectors.toList());

            billDTO.setBillDetailDTOList(billDetailDTOs);

            billDTOList.add(billDTO);
        }

        return billDTOList;
    }
    public List<BillDTO> getBillFromUserId(Long userId) {
        List<Object[]> billDetails = billRepository.getAllBillDetailsByUserId(userId);
        List<BillDTO> billDTOList = new ArrayList<>();

        for (Object[] billDetail : billDetails) {
            BillDTO billDTO = new BillDTO();

            // Mapping data from the query result to the BillDTO fields
            billDTO.setId((Long) billDetail[0]); // Assuming id is at index 0 in the query result
            billDTO.setTotalPrice((float) billDetail[1]); // Assuming totalPrice is at index 1 in the query result
            billDTO.setUserId((Long) billDetail[2]); // Assuming user_id is at index 2 in the query result
            billDTO.setDeliverAddress((String) billDetail[3]); // Assuming deliverAddress is at index 3 in the query result

            // Extract receipt details similarly to the getAllReceiptDetailsWithTotalAndSupplierId method
            List<BillDetail> billDetailList = new ArrayList<>();
            if (billDetail[4] instanceof BillDetail) {
                BillDetail singleBillDetail = (BillDetail) billDetail[4];
                billDetailList.add(singleBillDetail);
            } else if (billDetail[4] instanceof List<?>) {
                // If index 4 returns a list of BillDetail
                billDetailList = (List<BillDetail>) billDetail[4];
            }
            billDTO.setVoucherId((Long) billDetail[5]);
            List<BillDetailDTO> billDetailDTOs = billDetailList.stream()
                    .map(billDetailItem -> modelMapper.map(billDetailItem, BillDetailDTO.class))
                    .collect(Collectors.toList());

            billDTO.setBillDetailDTOList(billDetailDTOs);

            billDTOList.add(billDTO);
        }

        return billDTOList;
    }
    public Bill createBill(BillDTO billDTO) {

        Optional<User> userOptional = userRepository.findById(billDTO.getUserId());
        User user = userOptional.get();
        System.out.println("username: " + user.getDisplayName());
        Bill newBill = new Bill();
        newBill.setUser(user);
        newBill.setDeliverAddress(billDTO.getDeliverAddress());

        newBill.setCreatedBy(user.getDisplayName());
        newBill.setActive(true);
        newBill.setStatus("Đã đặt hàng");

        List<BillDetail> savedBillDetails = new ArrayList<>();


        //float total = 0;
        for (BillDetailDTO billDetailDTO : billDTO.getBillDetailDTOList()) {
            Product product = productService.getProductById(billDetailDTO.getProductId());
            BillDetail billDetail = new BillDetail();
            billDetail.setBill(newBill);
            billDetail.setProduct(product);
            int quantityToSubtract = billDetailDTO.getQuantity();
            int currentQuantity = product.getQuantity();
            int updatedQuantity = currentQuantity - quantityToSubtract;
            if (updatedQuantity >= 0) {
                product.setQuantity(updatedQuantity);
                productService.saveProduct(product); // Cập nhật số lượng sản phẩm trong kho
                billDetail.setActive(true);
                savedBillDetails.add(billDetail);
            } else {
                new Exception("Số lượng sản phẩm không đủ để thực hiện giao dịch!");
            }

            billDetail.setQuantity(billDetailDTO.getQuantity());
            float unitPrice = product.getPrice();
            billDetail.setUnitPrice(unitPrice);
            //total += unitPrice * billDetailDTO.getQuantity();

            billDetail.setActive(true);
            savedBillDetails.add(billDetail);

        }
        //  System.out.println("total: " + total);

        newBill.setTotalPrice(billDTO.getTotalPrice());
        if (billDTO.getVoucherId() != null) {
            Voucher voucher = voucherService.getVoucherFromId(billDTO.getVoucherId());
            newBill.setVoucher(voucher);
        }

//        if (billDTO.getVoucherId() != null ) {
//            voucherService.applyVoucher(newBill, billDTO.getVoucherId());
//        }
        newBill.setBillDetailList(savedBillDetails);
        Bill savedBill = billRepository.save(newBill);

        return savedBill;

    }

    public Bill createBill(Bill bill, Long userId, Long voucherId) {

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
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
        } else {
            System.out.println("Không tìm thấy userID : " + userId);
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

    public Bill getBillById(Long id) {
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

    public List<Bill> getBillList() {
        return billRepository.findAll();
    }

    public Bill updateBill(Long id, Bill bill) {
        Bill existingBill = billRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn với id: " + id));
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
