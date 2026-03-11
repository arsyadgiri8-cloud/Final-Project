package com.arsyad.asset_management.service;

import com.arsyad.asset_management.dto.AssetRequest;
import com.arsyad.asset_management.dto.AssetResponse;
import com.arsyad.asset_management.dto.MonitoringResponse;
import com.arsyad.asset_management.entity.*;
import com.arsyad.asset_management.repository.AssetRepository;
import com.arsyad.asset_management.repository.AssignmentRepository;
import com.arsyad.asset_management.repository.MaintenanceRepository;
import com.arsyad.asset_management.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final MaintenanceRepository maintenanceRepository;


    // CREATE

    public AssetResponse create(AssetRequest request) {

        Asset asset = new Asset();
        asset.setName(request.getName());
        asset.setSerialNumber(request.getSerialNumber());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setCreatedAt(LocalDateTime.now());

        return mapToResponse(assetRepository.save(asset));
    }

    // PAGINATION (ROLE BASED)

    public Page<AssetResponse> getAssetsWithPagination(int page, int size) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<Asset> assetPage;

        if (isAdmin) {
            assetPage = assetRepository.findByDeletedAtIsNull(pageable);
        } else {
            assetPage = assetRepository
                    .findByAssignmentsUserEmailAndAssignmentsStatusAndDeletedAtIsNull(
                            email,
                            AssignmentStatus.ACTIVE,
                            pageable
                    );
        }

        return assetPage.map(this::mapToResponse);
    }

    // ASSIGN (ENTERPRISE FLOW)

    public AssetResponse assignAsset(Long assetId, Long userId) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (asset.getStatus() == AssetStatus.RETIRED) {
            throw new RuntimeException("Cannot assign retired asset");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tutup assignment lama
        assignmentRepository
                .findByAssetIdAndStatus(assetId, AssignmentStatus.ACTIVE)
                .ifPresent(oldAssignment -> {
                    oldAssignment.setStatus(AssignmentStatus.REPLACED);
                    oldAssignment.setEndDate(LocalDate.now());
                    assignmentRepository.save(oldAssignment);
                });

        // Buat assignment baru
        Assignment newAssignment = new Assignment();
        newAssignment.setAsset(asset);
        newAssignment.setUser(user);
        newAssignment.setStartDate(LocalDate.now());
        newAssignment.setStatus(AssignmentStatus.ACTIVE);

        assignmentRepository.save(newAssignment);

        // Update asset
        asset.setStatus(AssetStatus.ASSIGNED);
        asset.setUpdatedAt(LocalDateTime.now());
        assetRepository.save(asset);

        return mapToResponse(asset);
    }

    // ASSIGNMENT HISTORY

    public List<Assignment> getAssignmentHistory(Long assetId) {
        return assignmentRepository
                .findByAssetIdOrderByStartDateDesc(assetId);
    }

    public List<Assignment> getUserAssignmentHistory(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }

    // USAGE DURATION

    public long calculateUsageDays(Long assetId) {

        List<Assignment> assignments =
                assignmentRepository.findByAssetIdOrderByStartDateDesc(assetId);

        long totalDays = 0;

        for (Assignment a : assignments) {

            if (a.getStartDate() != null) {

                LocalDate end =
                        a.getEndDate() != null
                                ? a.getEndDate()
                                : LocalDate.now();

                totalDays += ChronoUnit.DAYS
                        .between(a.getStartDate(), end);
            }
        }

        return totalDays;
    }

    // UPDATE STATUS

    public AssetResponse updateStatus(Long id, AssetStatus status) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        asset.setStatus(status);
        asset.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(assetRepository.save(asset));
    }

    // MAINTENANCE COMPLETE
    public void completeMaintenance(Long maintenanceId, boolean isBroken) {

        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        maintenance.setStatus(MaintenanceStatus.DONE);
        maintenanceRepository.save(maintenance);

    }

    // DASHBOARD

    public MonitoringResponse getMonitoringSummary() {

        return MonitoringResponse.builder()
                .totalAsset(assetRepository.countByDeletedAtIsNull())
                .available(assetRepository
                        .countByStatusAndDeletedAtIsNull(AssetStatus.AVAILABLE))
                .assigned(assetRepository
                        .countByStatusAndDeletedAtIsNull(AssetStatus.ASSIGNED))
                .maintenance(assetRepository
                        .countByStatusAndDeletedAtIsNull(AssetStatus.MAINTENANCE))
                .retired(assetRepository
                        .countByStatusAndDeletedAtIsNull(AssetStatus.RETIRED))
                .build();
    }

    // MAPPING

    private AssetResponse mapToResponse(Asset asset) {

        String assignedEmail = assignmentRepository
                .findByAssetIdAndStatus(asset.getId(), AssignmentStatus.ACTIVE)
                .map(a -> a.getUser().getEmail())
                .orElse(null);

        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .serialNumber(asset.getSerialNumber())
                .status(asset.getStatus())
                .assignedTo(assignedEmail)
                .build();
    }

    // ADMIN FILTER BY STATUS

    public Page<AssetResponse> getAssetsByStatus(
            AssetStatus status,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return assetRepository
                .findByStatusAndDeletedAtIsNull(status, pageable)
                .map(this::mapToResponse);
    }

    //DELETE
    public void deleteAsset(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        assetRepository.delete(asset);
    }

    //CREATE MAINTENANCE
    public void createMaintenance(Long assetId, LocalDate maintenanceDate) {

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (maintenanceRepository.existsByAssetIdAndStatus(assetId, MaintenanceStatus.PENDING)) {
            throw new RuntimeException("Asset already has pending maintenance");
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setAsset(asset);
        maintenance.setMaintenanceDate(maintenanceDate);
        maintenance.setStatus(MaintenanceStatus.PENDING);

        maintenanceRepository.save(maintenance);

        asset.setStatus(AssetStatus.MAINTENANCE);
        assetRepository.save(asset);
    }
}
