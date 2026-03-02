package com.arsyad.asset_management.repository;

import com.arsyad.asset_management.entity.Asset;
import com.arsyad.asset_management.entity.AssetStatus;
import com.arsyad.asset_management.entity.AssignmentStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {

   Page<Asset> findByStatusAndDeletedAtIsNull(
           AssetStatus status,
           Pageable pageable
   );

   List<Asset> findByNextMaintenanceDateAndDeletedAtIsNull(LocalDate date);
   List<Asset> findByDeletedAtIsNull();
   Page<Asset> findByDeletedAtIsNull(Pageable pageable);
   Page<Asset> findByAssignmentsUserEmailAndAssignmentsStatusAndDeletedAtIsNull(
           String email,
           AssignmentStatus status,
           Pageable pageable
   );

   long countByDeletedAtIsNull();
   long countByStatusAndDeletedAtIsNull(AssetStatus status);
}
