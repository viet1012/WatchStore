package com.ecommerce.WatchStore.Entities;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "role_models")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String roleTitle;

    private String description;

    private String created_by;

    private LocalDateTime created_dt;

    private String updated_by;

    private LocalDateTime updated_dt;

    private Boolean active;


    // getters and setters
}
