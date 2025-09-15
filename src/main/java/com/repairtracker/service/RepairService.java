package com.repairtracker.service;

import com.repairtracker.dto.request.CreateRepairRequest;
import com.repairtracker.dto.request.UpdateRepairStatusRequest;
import com.repairtracker.dto.response.RepairDto;
import com.repairtracker.entity.Repair;
import com.repairtracker.entity.RepairTimeline;
import com.repairtracker.entity.User;
import com.repairtracker.enums.RepairStatus;
import com.repairtracker.exception.BusinessException;
import com.repairtracker.repository.RepairRepository;
import com.repairtracker.repository.RepairTimelineRepository;
import com.repairtracker.repository.UserRepository;
import com.repairtracker.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepairService {
    
    private final RepairRepository repairRepository;
    private final RepairTimelineRepository repairTimelineRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    
    @Transactional
    public RepairDto createRepair(CreateRepairRequest request, User currentUser) {
        Repair repair = new Repair();
        repair.setCustomerName(request.getCustomerName());
        repair.setCustomerPhone(request.getCustomerPhone());
        repair.setCustomerEmail(request.getCustomerEmail());
        repair.setDeviceType(request.getDeviceType());
        repair.setDeviceModel(request.getDeviceModel());
        repair.setSerialNumber(request.getSerialNumber());
        repair.setProblemDescription(request.getProblemDescription());
        repair.setEstimatedCost(request.getEstimatedCost());
        
        repair.setStore(storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new BusinessException("Store not found")));
        
        if (request.getAssignedTechnicianId() != null) {
            repair.setAssignedTechnician(userRepository.findById(request.getAssignedTechnicianId())
                    .orElseThrow(() -> new BusinessException("Technician not found")));
        }
        
        Repair savedRepair = repairRepository.save(repair);
        
        // Create initial timeline entry
        RepairTimeline timeline = new RepairTimeline();
        timeline.setRepair(savedRepair);
        timeline.setStatus(RepairStatus.PENDING);
        timeline.setDescription("Repair request created");
        timeline.setUpdatedBy(currentUser);
        repairTimelineRepository.save(timeline);
        
        return convertToRepairDto(savedRepair);
    }
    
    @Transactional
    public RepairDto updateRepairStatus(Long repairId, UpdateRepairStatusRequest request, User currentUser) {
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new BusinessException("Repair not found"));
        
        repair.setStatus(request.getStatus());
        if (request.getActualCost() != null) {
            repair.setActualCost(request.getActualCost());
        }
        if (request.getAssignedTechnicianId() != null) {
            repair.setAssignedTechnician(userRepository.findById(request.getAssignedTechnicianId())
                    .orElseThrow(() -> new BusinessException("Technician not found")));
        }
        
        Repair savedRepair = repairRepository.save(repair);
        
        // Create timeline entry
        RepairTimeline timeline = new RepairTimeline();
        timeline.setRepair(savedRepair);
        timeline.setStatus(request.getStatus());
        timeline.setDescription(request.getDescription());
        timeline.setUpdatedBy(currentUser);
        repairTimelineRepository.save(timeline);
        
        return convertToRepairDto(savedRepair);
    }
    
    public RepairDto getRepairById(Long repairId) {
        Repair repair = repairRepository.findById(repairId)
                .orElseThrow(() -> new BusinessException("Repair not found"));
        return convertToRepairDto(repair);
    }
    
    public List<RepairDto> getAllRepairs() {
        return repairRepository.findAll().stream()
                .map(this::convertToRepairDto)
                .collect(Collectors.toList());
    }
    
    public List<RepairDto> getRepairsByStore(Long storeId) {
        return repairRepository.findByStoreId(storeId).stream()
                .map(this::convertToRepairDto)
                .collect(Collectors.toList());
    }
    
    public List<RepairDto> getRepairsByTechnician(Long technicianId) {
        return repairRepository.findByAssignedTechnicianId(technicianId).stream()
                .map(this::convertToRepairDto)
                .collect(Collectors.toList());
    }
    
    public List<RepairDto> getRepairsByStatus(RepairStatus status) {
        return repairRepository.findByStatus(status).stream()
                .map(this::convertToRepairDto)
                .collect(Collectors.toList());
    }
    
    private RepairDto convertToRepairDto(Repair repair) {
        RepairDto repairDto = new RepairDto();
        repairDto.setId(repair.getId());
        repairDto.setRepairNumber(repair.getRepairNumber());
        repairDto.setCustomerName(repair.getCustomerName());
        repairDto.setCustomerPhone(repair.getCustomerPhone());
        repairDto.setCustomerEmail(repair.getCustomerEmail());
        repairDto.setDeviceType(repair.getDeviceType());
        repairDto.setDeviceModel(repair.getDeviceModel());
        repairDto.setSerialNumber(repair.getSerialNumber());
        repairDto.setProblemDescription(repair.getProblemDescription());
        repairDto.setEstimatedCost(repair.getEstimatedCost());
        repairDto.setActualCost(repair.getActualCost());
        repairDto.setStatus(repair.getStatus());
        repairDto.setCreatedAt(repair.getCreatedAt());
        repairDto.setUpdatedAt(repair.getUpdatedAt());
        repairDto.setCompletedAt(repair.getCompletedAt());
        
        if (repair.getStore() != null) {
            repairDto.setStoreId(repair.getStore().getId());
            repairDto.setStoreName(repair.getStore().getName());
        }
        
        if (repair.getAssignedTechnician() != null) {
            repairDto.setAssignedTechnicianId(repair.getAssignedTechnician().getId());
            repairDto.setAssignedTechnicianName(repair.getAssignedTechnician().getFirstName() + " " + 
                    repair.getAssignedTechnician().getLastName());
        }
        
        return repairDto;
    }
}
