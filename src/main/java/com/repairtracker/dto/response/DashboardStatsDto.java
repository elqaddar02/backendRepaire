package com.repairtracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    
    private Long totalRepairs;
    private Long pendingRepairs;
    private Long inProgressRepairs;
    private Long completedRepairs;
    private Long totalUsers;
    private Long totalStores;
    private Double averageRepairTime;
    private Double totalRevenue;
}
