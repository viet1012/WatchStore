package com.ecommerce.WatchStore.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity(name = "product_models")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "img")
    private String img;

    @Column(name = "price")
    private float price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;


    @Column(name = "created_by")
    private String createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updatedDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "code")
    private String code;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "color")
    private String color;

    @ManyToOne
    @JoinColumn(name = "accessory_id", referencedColumnName = "id")
    private Accessory accessory;

    @Column(name = "description")
    private String description;


}

