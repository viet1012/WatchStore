package com.ecommerce.WatchStore.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    // Constructors, getters, and setters
}
