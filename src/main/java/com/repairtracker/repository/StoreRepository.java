package com.repairtracker.repository;

import com.repairtracker.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    Optional<Store> findByName(String name);
    
    List<Store> findByIsActiveTrue();
    
    @Query("SELECT s FROM Store s WHERE s.isActive = true ORDER BY s.name")
    List<Store> findAllActiveStores();
    
    @Query("SELECT COUNT(s) FROM Store s WHERE s.isActive = true")
    Long countActiveStores();
}
