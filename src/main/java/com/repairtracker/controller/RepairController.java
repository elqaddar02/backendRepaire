package com.repairtracker.controller;

import com.repairtracker.dto.request.CreateRepairRequest;
import com.repairtracker.dto.request.UpdateRepairStatusRequest;
import com.repairtracker.dto.response.RepairDto;
import com.repairtracker.entity.User;
import com.repairtracker.enums.RepairStatus;
import com.repairtracker.service.RepairService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repairs")
@RequiredArgsConstructor
public class RepairController {
    
    private final RepairService repairService;
    
    @PostMapping
    public ResponseEntity<RepairDto> createRepair(@Valid @RequestBody CreateRepairRequest request,
                                                  @AuthenticationPrincipal User currentUser) {
        RepairDto repair = repairService.createRepair(request, currentUser);
        return ResponseEntity.ok(repair);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RepairDto> getRepairById(@PathVariable Long id) {
        RepairDto repair = repairService.getRepairById(id);
        return ResponseEntity.ok(repair);
    }
    
    @GetMapping
    public ResponseEntity<List<RepairDto>> getAllRepairs() {
        List<RepairDto> repairs = repairService.getAllRepairs();
        return ResponseEntity.ok(repairs);
    }
    
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<RepairDto>> getRepairsByStore(@PathVariable Long storeId) {
        List<RepairDto> repairs = repairService.getRepairsByStore(storeId);
        return ResponseEntity.ok(repairs);
    }
    
    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<RepairDto>> getRepairsByTechnician(@PathVariable Long technicianId) {
        List<RepairDto> repairs = repairService.getRepairsByTechnician(technicianId);
        return ResponseEntity.ok(repairs);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RepairDto>> getRepairsByStatus(@PathVariable RepairStatus status) {
        List<RepairDto> repairs = repairService.getRepairsByStatus(status);
        return ResponseEntity.ok(repairs);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<RepairDto> updateRepairStatus(@PathVariable Long id,
                                                        @Valid @RequestBody UpdateRepairStatusRequest request,
                                                        @AuthenticationPrincipal User currentUser) {
        RepairDto repair = repairService.updateRepairStatus(id, request, currentUser);
        return ResponseEntity.ok(repair);
    }
}
