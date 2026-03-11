package com.arsyad.asset_management.controller;

import com.arsyad.asset_management.dto.AssetRequest;
import com.arsyad.asset_management.entity.Asset;
import com.arsyad.asset_management.entity.AssetStatus;
import com.arsyad.asset_management.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    // CREATE (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AssetRequest request) {
        return ResponseEntity.ok(assetService.create(request));
    }

    // PAGINATION (ADMIN & EMPLOYEE)
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @GetMapping("/page")
    public ResponseEntity<?> getAssetsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<?> result = assetService.getAssetsWithPagination(page, size);

        return ResponseEntity.ok(
                Map.of(
                        "data", result.getContent(),
                        "pagination", Map.of(
                                "page", result.getNumber(),
                                "size", result.getSize(),
                                "totalElements", result.getTotalElements(),
                                "totalPages", result.getTotalPages()
                        )
                )
        );
    }

    // ASSIGN (ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{assetId}/assign/{userId}")
    public ResponseEntity<?> assignAsset(
            @PathVariable Long assetId,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                assetService.assignAsset(assetId, userId)
        );
    }

    // UPDATE STATUS (TECHNICIAN)
    @PreAuthorize("hasRole('TECHNICIAN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam AssetStatus status) {

        return ResponseEntity.ok(
                assetService.updateStatus(id, status)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<?> getByStatus(
            @RequestParam AssetStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                assetService.getAssetsByStatus(status, page, size)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok("Asset deleted");
    }
}
