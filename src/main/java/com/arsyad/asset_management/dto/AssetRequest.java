package com.arsyad.asset_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AssetRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String serialNumber;

    private LocalDate purchaseDate;
}
