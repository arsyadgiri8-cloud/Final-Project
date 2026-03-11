package com.arsyad.asset_management.controller;

import com.arsyad.asset_management.dto.MaintenanceRequest;
import com.arsyad.asset_management.dto.MaintenanceResponse;
import com.arsyad.asset_management.entity.Maintenance;
import com.arsyad.asset_management.entity.MaintenanceStatus;
import com.arsyad.asset_management.repository.MaintenanceRepository;
import com.arsyad.asset_management.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/technician/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceRepository maintenanceRepository;

    // Technician melihat maintenance queue
    @GetMapping("/queue")
    public List<Maintenance> getQueue() {

        return maintenanceRepository.findByStatus(MaintenanceStatus.PENDING);
    }

    // Technician menyelesaikan maintenance
    @PutMapping("/{id}/complete")
    public Map<String, String> completeMaintenance(@PathVariable Long id) {

        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        maintenance.setStatus(MaintenanceStatus.DONE);

        maintenanceRepository.save(maintenance);

        return Map.of(
                "message", "Maintenance completed successfully"
        );
    }
}
