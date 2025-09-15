package com.repairtracker.controller;

import com.repairtracker.dto.response.StoreDto;
import com.repairtracker.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    
    private final StoreService storeService;
    
    @PostMapping
    public ResponseEntity<StoreDto> createStore(@Valid @RequestBody StoreDto storeDto) {
        StoreDto store = storeService.createStore(storeDto);
        return ResponseEntity.ok(store);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id) {
        StoreDto store = storeService.getStoreById(id);
        return ResponseEntity.ok(store);
    }
    
    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllStores() {
        List<StoreDto> stores = storeService.getAllStores();
        return ResponseEntity.ok(stores);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<StoreDto>> getActiveStores() {
        List<StoreDto> stores = storeService.getActiveStores();
        return ResponseEntity.ok(stores);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id, @Valid @RequestBody StoreDto storeDto) {
        StoreDto store = storeService.updateStore(id, storeDto);
        return ResponseEntity.ok(store);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
