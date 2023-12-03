package com.ecommerce.WatchStore.Services;

import com.ecommerce.WatchStore.DTO.ReviewProductDTO;
import com.ecommerce.WatchStore.Entities.Product;
import com.ecommerce.WatchStore.Entities.ReviewProduct;
import com.ecommerce.WatchStore.Entities.User;
import com.ecommerce.WatchStore.Repositories.ReviewProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewProductService {
    @Autowired
    private ReviewProductRepository reviewProductRepository;
    @Autowired
    private  ProductService productService;
    @Autowired
    private  UserService userService;

    public long getTotal()
    {
        return reviewProductRepository.count();
    }
    public List<ReviewProductDTO> getAllReviews() {
        List<ReviewProduct> reviewProducts =  reviewProductRepository.findAll();
        List<ReviewProductDTO> reviewProductDTOS  = new ArrayList<>();
        for (ReviewProduct reviewProduct : reviewProducts)
        {
            ReviewProductDTO reviewProductDTO = new ReviewProductDTO();
            reviewProductDTO.setId(reviewProduct.getId());
            reviewProductDTO.setProductId(reviewProduct.getProduct() !=null ? reviewProduct.getProduct().getProductId() : null);
            reviewProductDTO.setRating(reviewProduct.getRating());
            reviewProductDTO.setUserId(reviewProduct.getUser() != null ? reviewProduct.getUser().getId() : null);
            reviewProductDTO.setComment(reviewProduct.getComment());
            reviewProductDTO.setCreatedDt(reviewProduct.getCreatedDt());
            reviewProductDTOS.add(reviewProductDTO);
        }
        return reviewProductDTOS;
    }
    public List<ReviewProduct> getReviewsByProductId(Long productId) {
        return reviewProductRepository.findByProductId(productId);
    }
    public ReviewProduct getReviewById(Long id) {
        return reviewProductRepository.findById(id).orElse(null);
    }

    public ReviewProduct createReview(ReviewProductDTO reviewProductDTO) {
        ReviewProduct savedRP =  new ReviewProduct();
        Product product = productService.getProductById(reviewProductDTO.getProductId());
        User user = userService.getUserById(reviewProductDTO.getUserId());
        savedRP.setProduct(product);
        savedRP.setComment(reviewProductDTO.getComment());
        savedRP.setRating(reviewProductDTO.getRating());
        savedRP.setUser(user);
        return reviewProductRepository.save(savedRP);
    }

    public ReviewProduct updateReview(Long id, ReviewProduct updatedReview) {
        if (reviewProductRepository.existsById(id)) {
            updatedReview.setId(id);
            return reviewProductRepository.save(updatedReview);
        }
        return null;
    }

    public void deleteReview(Long id) {
        reviewProductRepository.deleteById(id);
    }
}
