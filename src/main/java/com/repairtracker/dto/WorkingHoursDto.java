package com.repairtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHoursDto {
    
    private Long id;
    private Long userId;
    private String userName;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer breakDurationMinutes;
    private Double totalHours;
    private String notes;
}
