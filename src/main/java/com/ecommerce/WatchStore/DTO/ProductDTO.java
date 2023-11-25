package com.ecommerce.WatchStore.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ProductDTO {
    private Long productId;
    private String productName;
    private String img;
    private float price;
    private Integer quantity;
    private Long brandId; // Thay đổi brand thành brandId
    private Long categoryId; //Thêm categoryId để tham chiếu đến ID của category
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private Boolean active;
    private String code;
    private String thumbnail;
    private String gender;
    private String status;
    private String color;
    private Long accessoryId; // Thêm accessoryId để tham chiếu đến ID của accessory
    private String description;


}
