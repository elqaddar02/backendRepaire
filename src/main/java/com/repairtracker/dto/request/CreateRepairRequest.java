package com.repairtracker.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRepairRequest {
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    private String customerPhone;
    
    @Email(message = "Customer email should be valid")
    private String customerEmail;
    
    @NotBlank(message = "Device type is required")
    private String deviceType;
    
    private String deviceModel;
    
    private String serialNumber;
    
    @NotBlank(message = "Problem description is required")
    private String problemDescription;
    
    @Positive(message = "Estimated cost must be positive")
    private BigDecimal estimatedCost;
    
    @NotNull(message = "Store ID is required")
    private Long storeId;
    
    private Long assignedTechnicianId;
}
