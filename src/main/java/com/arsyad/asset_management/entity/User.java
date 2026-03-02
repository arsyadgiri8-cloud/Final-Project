package com.arsyad.asset_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime deletedAt;
}
