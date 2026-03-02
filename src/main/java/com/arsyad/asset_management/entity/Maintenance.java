package com.arsyad.asset_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Asset asset;

    private LocalDate maintenanceDate;

    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
}
