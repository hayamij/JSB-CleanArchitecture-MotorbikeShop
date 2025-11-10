package com.motorbike.persistence.gateway;

import com.motorbike.persistence.entity.ProductJpaEntity;
import com.motorbike.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPA implementation of ProductPersistenceGateway
 * This class belongs to Persistence Layer
 * Returns raw data (Map) - NO dependency on Business Layer entities
 */
@Component
public class ProductJpaGateway implements ProductPersistenceGateway {
    
    private final ProductJpaRepository jpaRepository;
    
    public ProductJpaGateway(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<Map<String, Object>> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toMap);
    }
    
    @Override
    public List<Map<String, Object>> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Map<String, Object>> findAvailableProducts() {
        return jpaRepository.findAvailableProducts().stream()
                .map(this::toMap)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> save(Map<String, Object> productData) {
        ProductJpaEntity entity = toEntity(productData);
        ProductJpaEntity saved = jpaRepository.save(entity);
        return toMap(saved);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
    
    // ===============================================
    // PRIVATE METHODS - Convert JPA Entity <-> Map
    // ===============================================
    
    /**
     * Convert JPA Entity to Map
     */
    private Map<String, Object> toMap(ProductJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("name", entity.getName());
        map.put("description", entity.getDescription());
        map.put("price", entity.getPrice());
        map.put("imageUrl", entity.getImageUrl());
        map.put("specifications", entity.getSpecifications());
        map.put("category", entity.getCategory());
        map.put("stockQuantity", entity.getStockQuantity());
        map.put("available", entity.isAvailable());
        map.put("createdAt", entity.getCreatedAt());
        map.put("updatedAt", entity.getUpdatedAt());
        
        return map;
    }
    
    /**
     * Convert Map to JPA Entity
     */
    private ProductJpaEntity toEntity(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId((Long) map.get("id"));
        entity.setName((String) map.get("name"));
        entity.setDescription((String) map.get("description"));
        entity.setPrice((java.math.BigDecimal) map.get("price"));
        entity.setImageUrl((String) map.get("imageUrl"));
        entity.setSpecifications((String) map.get("specifications"));
        entity.setCategory((String) map.get("category"));
        entity.setStockQuantity((Integer) map.get("stockQuantity"));
        entity.setAvailable((Boolean) map.get("available"));
        entity.setCreatedAt((LocalDateTime) map.get("createdAt"));
        entity.setUpdatedAt((LocalDateTime) map.get("updatedAt"));
        
        return entity;
    }
}
