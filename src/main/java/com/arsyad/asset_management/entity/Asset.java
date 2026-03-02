package com.arsyad.asset_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "assets")
@SQLDelete(sql = "UPDATE assets SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class Asset extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String serialNumber;

    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    private LocalDate purchaseDate;

    @ManyToOne
    @JoinColumn(name = "assigned_to")

    @OneToMany(mappedBy = "asset")
    private List<Assignment> assignments;

    private Integer maintenanceInterval; // dalam hari

    private LocalDate lastMaintenanceDate;

    private LocalDate nextMaintenanceDate;

}
