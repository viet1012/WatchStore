package com.ecommerce.WatchStore.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "address_models")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "ward")
    private String ward;
    @Column(name = "district")

    private String district;
    @Column(name = "houseNumber")

    private String houseNumber;
    @Column(name = "note")

    private String note;
    @Column(name = "active")

    private boolean active;

    // Getters and setters
}
