package com.repairtracker.entity;

import com.repairtracker.enums.RepairStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "repairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repair {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "repair_number", unique = true, nullable = false)
    private String repairNumber;
    
    @Column(name = "customer_name", nullable = false)
    private String customerName;
    
    @Column(name = "customer_phone")
    private String customerPhone;
    
    @Column(name = "customer_email")
    private String customerEmail;
    
    @Column(name = "device_type", nullable = false)
    private String deviceType;
    
    @Column(name = "device_model")
    private String deviceModel;
    
    @Column(name = "serial_number")
    private String serialNumber;
    
    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;
    
    @Column(name = "estimated_cost")
    private BigDecimal estimatedCost;
    
    @Column(name = "actual_cost")
    private BigDecimal actualCost;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepairStatus status = RepairStatus.PENDING;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_technician_id")
    private User assignedTechnician;
    
    @OneToMany(mappedBy = "repair", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RepairTimeline> timeline;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (repairNumber == null) {
            repairNumber = "REP-" + System.currentTimeMillis();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (status == RepairStatus.COMPLETED && completedAt == null) {
            completedAt = LocalDateTime.now();
        }
    }
}
