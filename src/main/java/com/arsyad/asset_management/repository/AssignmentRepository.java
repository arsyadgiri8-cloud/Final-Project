package com.arsyad.asset_management.repository;

import com.arsyad.asset_management.entity.Assignment;
import com.arsyad.asset_management.entity.AssignmentStatus;
import jakarta.persistence.OneToMany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Optional<Assignment> findByAssetIdAndStatus(Long assetId, AssignmentStatus status);
    List<Assignment> findByAssetIdOrderByStartDateDesc(Long assetId);
    List<Assignment> findByUserId(Long userId);
    List<Assignment> findByUserEmailAndStatus(String email, AssignmentStatus status);
}
