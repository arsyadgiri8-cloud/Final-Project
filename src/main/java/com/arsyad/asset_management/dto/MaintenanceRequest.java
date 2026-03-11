package com.arsyad.asset_management.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceRequest {

    private Long assetId;

    private LocalDate maintenanceDate;

}
