package com.arsyad.asset_management.config;

import com.arsyad.asset_management.entity.Asset;
import com.arsyad.asset_management.entity.AssetStatus;
import com.arsyad.asset_management.repository.AssetRepository;
import com.arsyad.asset_management.repository.MaintenanceRepository;
import com.arsyad.asset_management.entity.Maintenance;
import com.arsyad.asset_management.entity.MaintenanceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaintenanceScheduler {
    private final AssetRepository assetRepository;
    private final MaintenanceRepository maintenanceRepository;

    // Jalan setiap hari jam 00:00
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateMaintenanceTask() {

        log.info("Running maintenance scheduler...");

        List<Asset> dueAssets =
                assetRepository.findByNextMaintenanceDateAndDeletedAtIsNull(
                        LocalDate.now()
                );

        for (Asset asset : dueAssets) {

            boolean exists = maintenanceRepository
                    .existsByAssetIdAndStatus(
                            asset.getId(),
                            MaintenanceStatus.PENDING
                    );

            if (!exists) {

                Maintenance maintenance = new Maintenance();
                maintenance.setAsset(asset);
                maintenance.setStatus(MaintenanceStatus.PENDING);

                maintenanceRepository.save(maintenance);

                asset.setStatus(AssetStatus.MAINTENANCE);
                assetRepository.save(asset);

                log.info("Maintenance created for asset id={}", asset.getId());
            }
        }
    }
}