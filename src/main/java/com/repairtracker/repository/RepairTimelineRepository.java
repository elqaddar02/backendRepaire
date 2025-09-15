package com.repairtracker.repository;

import com.repairtracker.entity.RepairTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairTimelineRepository extends JpaRepository<RepairTimeline, Long> {
    
    List<RepairTimeline> findByRepairIdOrderByCreatedAtAsc(Long repairId);
    
    @Query("SELECT rt FROM RepairTimeline rt WHERE rt.repair.id = :repairId ORDER BY rt.createdAt ASC")
    List<RepairTimeline> findTimelineByRepairId(@Param("repairId") Long repairId);
    
    @Query("SELECT rt FROM RepairTimeline rt WHERE rt.updatedBy.id = :userId ORDER BY rt.createdAt DESC")
    List<RepairTimeline> findByUpdatedByOrderByCreatedAtDesc(@Param("userId") Long userId);
}
