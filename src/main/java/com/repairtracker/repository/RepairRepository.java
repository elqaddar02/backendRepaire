package com.repairtracker.repository;

import com.repairtracker.entity.Repair;
import com.repairtracker.enums.RepairStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepairRepository extends JpaRepository<Repair, Long> {
    
    Optional<Repair> findByRepairNumber(String repairNumber);
    
    List<Repair> findByStatus(RepairStatus status);
    
    List<Repair> findByStoreId(Long storeId);
    
    List<Repair> findByAssignedTechnicianId(Long technicianId);
    
    @Query("SELECT r FROM Repair r WHERE r.store.id = :storeId AND r.status = :status")
    List<Repair> findByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") RepairStatus status);
    
    @Query("SELECT r FROM Repair r WHERE r.assignedTechnician.id = :technicianId AND r.status = :status")
    List<Repair> findByTechnicianIdAndStatus(@Param("technicianId") Long technicianId, @Param("status") RepairStatus status);
    
    @Query("SELECT COUNT(r) FROM Repair r WHERE r.status = :status")
    Long countByStatus(@Param("status") RepairStatus status);
    
    @Query("SELECT COUNT(r) FROM Repair r WHERE r.store.id = :storeId AND r.status = :status")
    Long countByStoreIdAndStatus(@Param("storeId") Long storeId, @Param("status") RepairStatus status);
    
    @Query("SELECT r FROM Repair r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Repair> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT r FROM Repair r WHERE r.status = 'COMPLETED' AND r.completedAt IS NOT NULL")
    List<Repair> findCompletedRepairs();
}
