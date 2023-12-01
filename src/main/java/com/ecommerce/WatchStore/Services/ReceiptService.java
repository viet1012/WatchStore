package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.ReceiptDTO;
import com.ecommerce.WatchStore.DTO.ReceiptDetailDTO;
import com.ecommerce.WatchStore.Entities.*;
import com.ecommerce.WatchStore.Repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReceiptService {
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ModelMapper modelMapper;
    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }
    public List<ReceiptDTO> getAllReceiptDetailsWithTotalAndSupplierId() {
        List<Object[]> results = receiptRepository.getAllReceiptDetailsWithTotalAndSupplierId();
        List<ReceiptDTO> receiptInfoList = new ArrayList<>();

        for (Object[] result : results) {
            long id = (long) result[0];
            double total = (Double) result[1]; // Lấy total từ kết quả trả về
            Long supplierId = (Long) result[2]; // Lấy supplierId từ kết quả trả về

            List<ReceiptDetail> receiptDetails = new ArrayList<>();

            // Kiểm tra xem result[2] có phải là một ReceiptDetail không
            if (result[3] instanceof ReceiptDetail) {
                ReceiptDetail singleReceiptDetail = (ReceiptDetail) result[3];
                receiptDetails.add(singleReceiptDetail);
            } else if (result[3] instanceof List<?>) {
                // Nếu result[2] trả về một danh sách các ReceiptDetail
                receiptDetails = (List<ReceiptDetail>) result[3];
            }

            List<ReceiptDetailDTO> receiptDetailDTOs = receiptDetails.stream()
                    .map(receiptDetail -> modelMapper.map(receiptDetail, ReceiptDetailDTO.class))
                    .collect(Collectors.toList());

            ReceiptDTO receiptInfo = new ReceiptDTO();
            receiptInfo.setId(id);
            receiptInfo.setTotal(total);
            receiptInfo.setSupplierId(supplierId);
            receiptInfo.setReceiptDetails(receiptDetailDTOs);

            receiptInfoList.add(receiptInfo);
        }

        return receiptInfoList;
    }
    public List<ReceiptDTO> getAll() {
        List<Object[]> results = receiptRepository.getAllReceiptDetailsWithTotalAndSupplierId();
        Map<Long, ReceiptDTO> receiptMap = new HashMap<>();

        for (Object[] result : results) {
            long id = (long) result[0];
            double total = (Double) result[1]; // Lấy total từ kết quả trả về
            Long supplierId = (Long) result[2]; // Lấy supplierId từ kết quả trả về

            ReceiptDetail receiptDetail = (ReceiptDetail) result[3];

            ReceiptDTO receiptInfo = receiptMap.getOrDefault(id, new ReceiptDTO());
            receiptInfo.setId(id);
            receiptInfo.setTotal(total);
            receiptInfo.setSupplierId(supplierId);

            List<ReceiptDetailDTO> receiptDetailDTOs = receiptInfo.getReceiptDetails();
            if (receiptDetailDTOs == null) {
                receiptDetailDTOs = new ArrayList<>();
            }

            receiptDetailDTOs.add(modelMapper.map(receiptDetail, ReceiptDetailDTO.class));
            receiptInfo.setReceiptDetails(receiptDetailDTOs);

            receiptMap.put(id, receiptInfo);
        }

        return new ArrayList<>(receiptMap.values());
    }

    public Optional<Receipt> getReceiptById(Long id) {
        return receiptRepository.findById(id);
    }

    public Receipt createReceipt(ReceiptDTO receiptDTO) {

        System.out.println("SupplierId " + receiptDTO.getSupplierId());
        double total = 0;
        Optional<Supplier> supplier = supplierRepository.findById(receiptDTO.getSupplierId());
        Supplier supplierObj = supplier.orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        Optional<User> userOptional = userRepository.findById(receiptDTO.getUserId());
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found"));

        Receipt newReceipt = new Receipt();
        newReceipt.setSupplier(supplierObj);
        newReceipt.setCreatedBy(user.getDisplayName());
        newReceipt.setUser(user);


        List<ReceiptDetail> savedReceiptDetails = new ArrayList<>();

        for (ReceiptDetailDTO detailDTO : receiptDTO.getReceiptDetails()) {

            ReceiptDetail detail = new ReceiptDetail();
            Optional<Product> optionalProduct = productRepository.findById(detailDTO.getProductId());
            total += detailDTO.getPrice() * detailDTO.getQuantity();
            detail.setReceipt(newReceipt);
            detail.setProduct(optionalProduct.get());
            detail.setQuantity(detailDTO.getQuantity());
            detail.setPrice(detailDTO.getPrice());
            savedReceiptDetails.add(detail);
        }
        newReceipt.setTotal(total);
        newReceipt.setReceiptDetails(savedReceiptDetails);

        return receiptRepository.save(newReceipt);
    }



    public Receipt updateReceipt(Long id, Receipt updatedReceipt) {
        // Kiểm tra xem Receipt có tồn tại không
        Optional<Receipt> existingReceipt = receiptRepository.findById(id);

        if (existingReceipt.isPresent()) {
            Receipt receipt = existingReceipt.get();
            // Cập nhật thông tin của Receipt
            receipt.setSupplier(updatedReceipt.getSupplier());
            receipt.setUser(updatedReceipt.getUser());
            receipt.setTotal(updatedReceipt.getTotal());
            receipt.setUpdatedBy(updatedReceipt.getUpdatedBy());
            receipt.setActive(updatedReceipt.isActive());

            return receiptRepository.save(receipt);
        } else {
            // Xử lý khi Receipt không tồn tại
            return null;
        }
    }
    public void deleteReceipt(Long id) {
        // Xóa Receipt theo ID
        receiptRepository.deleteById(id);
    }
    public List<Receipt> getReceiptsByPage(int page, int pageSize) {
        // Trừ 1 để đảm bảo trang bắt đầu từ 0
        PageRequest pageable = PageRequest.of(page - 1, pageSize);
        Page<Receipt> receiptPage = receiptRepository.findAll(pageable);
        return receiptPage.getContent(); // Lấy danh sách receipt trên trang cụ thể.
    }

    public long getTotalReceipts() {
        return receiptRepository.count(); // Sử dụng phương thức count của JpaRepository để đếm tổng số receipt.
    }
}
