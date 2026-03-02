package com.arsyad.asset_management.controller;

import com.arsyad.asset_management.entity.AssetStatus;
import com.arsyad.asset_management.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.arsyad.asset_management.entity.AssetStatus;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AssetService assetService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monitoring")
    public ResponseEntity<?> monitoring() {
        return ResponseEntity.ok(assetService.getMonitoringSummary());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/monitoring/{status}")
    public ResponseEntity<?> monitoringByStatus(
            @PathVariable AssetStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                assetService.getAssetsByStatus(status, page, size)
        );
    }
}
