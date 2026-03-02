package com.arsyad.asset_management.service;

import com.arsyad.asset_management.dto.AssetRequest;
import com.arsyad.asset_management.dto.AssetResponse;
import com.arsyad.asset_management.entity.Asset;
import com.arsyad.asset_management.entity.AssetStatus;
import com.arsyad.asset_management.repository.AssetRepository;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.arsyad.asset_management.repository.AssignmentRepository;
import com.arsyad.asset_management.repository.MaintenanceRepository;
import com.arsyad.asset_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {
    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private AssetService assetService;

    @Test
    void create_shouldSetStatusAvailable() {

        Asset asset = new Asset();
        asset.setName("Laptop");

        when(assetRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // karena mapToResponse() sekarang pakai assignmentRepository
        when(assignmentRepository.findByAssetIdAndStatus(any(), any()))
                .thenReturn(Optional.empty());

        AssetRequest request = new AssetRequest();
        request.setName("Laptop");
        request.setSerialNumber("SN001");
        request.setPurchaseDate(LocalDate.now());

        AssetResponse result = assetService.create(request);

        assertEquals(AssetStatus.AVAILABLE, result.getStatus());
    }
}
