package com.ecommerce.WatchStore.Controllers;

import com.ecommerce.WatchStore.DTO.BillPageDTO;
import com.ecommerce.WatchStore.DTO.ReviewProductDTO;
import com.ecommerce.WatchStore.Entities.ReviewProduct;
import com.ecommerce.WatchStore.Response.ResponseWrapper;
import com.ecommerce.WatchStore.Services.ReviewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/Review_product")
public class ReviewProductController {
    @Autowired
    private  ReviewProductService reviewProductService;


    @GetMapping("/GetAll")
    public ResponseEntity<ResponseWrapper<List<ReviewProductDTO>>> getAllReviews() {
        List<ReviewProductDTO> reviews = reviewProductService.getAllReviews();
        long total = reviewProductService.getTotal();;
        ResponseWrapper<List<ReviewProductDTO>> response = new ResponseWrapper<>(HttpStatus.OK.value(), "Success", true,total, reviews);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/GetById/{id}")
    public ResponseEntity<ReviewProduct> getReviewById(@PathVariable Long id) {
        ReviewProduct review = reviewProductService.getReviewById(id);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/GetByProductId/{id}")
    public ResponseEntity< List<ReviewProduct>> getReviewByProductId(@PathVariable Long id) {
        List<ReviewProduct> review = reviewProductService.getReviewsByProductId(id);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/Create")
    public ResponseEntity<ReviewProduct> createReview(@RequestBody ReviewProductDTO reviewProductModel) {
        ReviewProduct createdReview = reviewProductService.createReview(reviewProductModel);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }


    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewProductService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
