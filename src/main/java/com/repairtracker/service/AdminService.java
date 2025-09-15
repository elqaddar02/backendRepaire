package com.repairtracker.service;

import com.repairtracker.dto.response.DashboardStatsDto;
import com.repairtracker.dto.response.UserDto;
import com.repairtracker.entity.Repair;
import com.repairtracker.entity.User;
import com.repairtracker.enums.RepairStatus;
import com.repairtracker.enums.UserRole;
import com.repairtracker.repository.RepairRepository;
import com.repairtracker.repository.StoreRepository;
import com.repairtracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final UserRepository userRepository;
    private final RepairRepository repairRepository;
    private final StoreRepository storeRepository;
    
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        stats.setTotalRepairs(repairRepository.count());
        stats.setPendingRepairs(repairRepository.countByStatus(RepairStatus.PENDING));
        stats.setInProgressRepairs(repairRepository.countByStatus(RepairStatus.IN_PROGRESS));
        stats.setCompletedRepairs(repairRepository.countByStatus(RepairStatus.COMPLETED));
        stats.setTotalUsers(userRepository.count());
        stats.setTotalStores(storeRepository.countActiveStores());
        
        // Calculate average repair time manually
        List<Repair> completedRepairs = repairRepository.findCompletedRepairs();
        double avgRepairTime = completedRepairs.stream()
                .mapToDouble(repair -> {
                    if (repair.getCompletedAt() != null && repair.getCreatedAt() != null) {
                        return java.time.Duration.between(repair.getCreatedAt(), repair.getCompletedAt()).toHours();
                    }
                    return 0.0;
                })
                .filter(hours -> hours > 0)
                .average()
                .orElse(0.0);
        stats.setAverageRepairTime(avgRepairTime);
        
        // Calculate total revenue from completed repairs
        double totalRevenue = repairRepository.findByStatus(RepairStatus.COMPLETED)
                .stream()
                .filter(repair -> repair.getActualCost() != null)
                .mapToDouble(repair -> repair.getActualCost().doubleValue())
                .sum();
        stats.setTotalRevenue(totalRevenue);
        
        return stats;
    }
    
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    
    public List<UserDto> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    
    public List<UserDto> getUsersByStore(Long storeId) {
        return userRepository.findByStoreId(storeId).stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }
    
    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        
        if (user.getStore() != null) {
            userDto.setStoreId(user.getStore().getId());
            userDto.setStoreName(user.getStore().getName());
        }
        
        return userDto;
    }
}
