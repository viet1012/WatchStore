package com.ecommerce.WatchStore.Entities;

import jakarta.persistence.*;


@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String permissionTitle;

    private String description;

    public String getPermissionTitle() {
        return permissionTitle;
    }

    public void setPermissionTitle(String permissionTitle) {
        this.permissionTitle = permissionTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Các trường khác (createdBy, createdDate, updatedBy, updatedDate, active) tùy theo nhu cầu

    // getters and setters
}
