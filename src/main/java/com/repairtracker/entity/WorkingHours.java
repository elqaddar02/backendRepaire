package com.repairtracker.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "working_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHours {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;
    
    @Column(name = "start_time")
    private LocalTime startTime;
    
    @Column(name = "end_time")
    private LocalTime endTime;
    
    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes = 0;
    
    @Column(name = "total_hours")
    private Double totalHours;
    
    @Column(name = "notes")
    private String notes;
    
    @PrePersist
    @PreUpdate
    protected void calculateTotalHours() {
        if (startTime != null && endTime != null) {
            long totalMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
            totalMinutes -= (breakDurationMinutes != null ? breakDurationMinutes : 0);
            totalHours = totalMinutes / 60.0;
        }
    }
}
