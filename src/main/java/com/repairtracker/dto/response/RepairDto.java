package com.repairtracker.dto.response;

import com.repairtracker.enums.RepairStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairDto {
    
    private Long id;
    private String repairNumber;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String deviceType;
    private String deviceModel;
    private String serialNumber;
    private String problemDescription;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private RepairStatus status;
    private Long storeId;
    private String storeName;
    private Long assignedTechnicianId;
    private String assignedTechnicianName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}
