package com.repairtracker.service;

import com.repairtracker.dto.response.StoreDto;
import com.repairtracker.entity.Store;
import com.repairtracker.exception.BusinessException;
import com.repairtracker.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    
    private final StoreRepository storeRepository;
    
    @Transactional
    public StoreDto createStore(StoreDto storeDto) {
        Store store = new Store();
        store.setName(storeDto.getName());
        store.setAddress(storeDto.getAddress());
        store.setPhoneNumber(storeDto.getPhoneNumber());
        store.setEmail(storeDto.getEmail());
        store.setManagerName(storeDto.getManagerName());
        store.setIsActive(true);
        
        Store savedStore = storeRepository.save(store);
        return convertToStoreDto(savedStore);
    }
    
    @Transactional
    public StoreDto updateStore(Long storeId, StoreDto storeDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException("Store not found"));
        
        store.setName(storeDto.getName());
        store.setAddress(storeDto.getAddress());
        store.setPhoneNumber(storeDto.getPhoneNumber());
        store.setEmail(storeDto.getEmail());
        store.setManagerName(storeDto.getManagerName());
        store.setIsActive(storeDto.getIsActive());
        
        Store savedStore = storeRepository.save(store);
        return convertToStoreDto(savedStore);
    }
    
    public StoreDto getStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException("Store not found"));
        return convertToStoreDto(store);
    }
    
    public List<StoreDto> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::convertToStoreDto)
                .collect(Collectors.toList());
    }
    
    public List<StoreDto> getActiveStores() {
        return storeRepository.findByIsActiveTrue().stream()
                .map(this::convertToStoreDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void deleteStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException("Store not found"));
        
        store.setIsActive(false);
        storeRepository.save(store);
    }
    
    private StoreDto convertToStoreDto(Store store) {
        StoreDto storeDto = new StoreDto();
        storeDto.setId(store.getId());
        storeDto.setName(store.getName());
        storeDto.setAddress(store.getAddress());
        storeDto.setPhoneNumber(store.getPhoneNumber());
        storeDto.setEmail(store.getEmail());
        storeDto.setManagerName(store.getManagerName());
        storeDto.setIsActive(store.getIsActive());
        storeDto.setCreatedAt(store.getCreatedAt());
        storeDto.setUpdatedAt(store.getUpdatedAt());
        return storeDto;
    }
}
