package com.repairtracker.dto.request;

import com.repairtracker.enums.RepairStatus;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateRepairStatusRequest {
    
    @NotNull(message = "Status is required")
    private RepairStatus status;
    
    private String description;
    
    private BigDecimal actualCost;
    
    private Long assignedTechnicianId;
}
