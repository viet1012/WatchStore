package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.ReceiptDetailDTO;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.Receipt;
import com.ecommerce.WatchStore.Entities.ReceiptDetail;
import com.ecommerce.WatchStore.Repositories.ProductRepository;
import com.ecommerce.WatchStore.Repositories.ReceiptDetailRepository;
import com.ecommerce.WatchStore.Repositories.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
@Service
public class ReceiptDetailService {
    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ModelMapper modelMapper;

    public List<ReceiptDetailDTO> getAllReceiptDetails() {
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findAll();
        return receiptDetails.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReceiptDetailDTO convertToDto(ReceiptDetail receiptDetail) {
        return modelMapper.map(receiptDetail, ReceiptDetailDTO.class);
    }

    public Optional<ReceiptDetail> getReceiptDetailById(Long id) {
        return receiptDetailRepository.findById(id);
    }

    public ReceiptDetail createReceiptDetail(ReceiptDetail receiptDetail, long receiptId , long productId) {
        Optional<Receipt> optionalReceipt = receiptRepository.findById(receiptId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        Product product = optionalProduct.get();

        ReceiptDetail newReceiptDetail = new ReceiptDetail();

        if(optionalReceipt.isEmpty())
        {
            newReceiptDetail.setReceipt(null);
        }
        else {
            newReceiptDetail.setReceipt(optionalReceipt.get());
        }
        if(optionalProduct.isEmpty())
        {
            newReceiptDetail.setProduct(null);
        }
        else {

            newReceiptDetail.setProduct(product);
        }
        newReceiptDetail.setQuantity(receiptDetail.getQuantity());
        optionalProduct.get().setQuantity(newReceiptDetail.getQuantity());

        newReceiptDetail.setPrice(receiptDetail.getPrice());
        optionalProduct.get().setPrice((float) newReceiptDetail.getPrice());

        productRepository.save(optionalProduct.get());
        newReceiptDetail.setActive(true);
        return receiptDetailRepository.save(newReceiptDetail);


    }

    public ReceiptDetail updateReceiptDetail(Long id, ReceiptDetail updatedReceiptDetail) {
        // Kiểm tra xem ReceiptDetail có tồn tại không
        Optional<ReceiptDetail> existingReceiptDetail = receiptDetailRepository.findById(id);

        if (existingReceiptDetail.isPresent()) {
            ReceiptDetail receiptDetail = existingReceiptDetail.get();
            // Cập nhật thông tin của ReceiptDetail
            receiptDetail.setReceipt(updatedReceiptDetail.getReceipt());
            receiptDetail.setProduct(updatedReceiptDetail.getProduct());
            receiptDetail.setQuantity(updatedReceiptDetail.getQuantity());
            receiptDetail.setPrice(updatedReceiptDetail.getPrice());
            receiptDetail.setUpdatedBy(updatedReceiptDetail.getUpdatedBy());
            receiptDetail.setActive(updatedReceiptDetail.isActive());

            return receiptDetailRepository.save(receiptDetail);
        } else {
            // Xử lý khi ReceiptDetail không tồn tại
            return null;
        }
    }

    public void deleteReceiptDetail(Long id) {
        // Xóa ReceiptDetail theo ID
        receiptDetailRepository.deleteById(id);
    }
}
