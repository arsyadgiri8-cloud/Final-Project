package com.arsyad.asset_management.repository;

import com.arsyad.asset_management.entity.Maintenance;
import com.arsyad.asset_management.entity.MaintenanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByStatus(MaintenanceStatus status);
    boolean existsByAssetIdAndStatus(Long assetId, MaintenanceStatus status);

}
