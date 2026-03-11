package com.arsyad.asset_management.dto;

import com.arsyad.asset_management.entity.MaintenanceStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public record MaintenanceResponse(
        Long id,
        String assetName,
        LocalDate maintenanceDate,
        MaintenanceStatus status
) {
}
