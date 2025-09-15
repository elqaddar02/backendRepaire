package com.repairtracker.repository;

import com.repairtracker.entity.User;
import com.repairtracker.enums.UserRole;
import com.repairtracker.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByStatus(UserStatus status);
    
    List<User> findByStoreId(Long storeId);
    
    @Query("SELECT u FROM User u WHERE u.store.id = :storeId AND u.role = :role")
    List<User> findByStoreIdAndRole(@Param("storeId") Long storeId, @Param("role") UserRole role);
    
    @Query("SELECT u FROM User u WHERE u.role = 'TECHNICIAN' AND u.status = 'ACTIVE'")
    List<User> findActiveTechnicians();
}
