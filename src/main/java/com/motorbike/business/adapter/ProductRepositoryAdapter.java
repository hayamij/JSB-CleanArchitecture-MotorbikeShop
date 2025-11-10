package com.motorbike.business.adapter;

import com.motorbike.business.entity.Product;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.persistence.gateway.ProductPersistenceGateway;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementation of ProductRepository
 * This adapter belongs to BUSINESS LAYER
 * Uses ProductPersistenceGateway from Persistence Layer (via interface ONLY)
 * Gateway returns raw data (Map), Adapter converts to Domain entities
 * 
 * KEY POINT: NO import of Persistence entities!
 * Business Layer only knows about Gateway interface
 * Persistence Layer does NOT depend on Business Layer
 */
public class ProductRepositoryAdapter implements ProductRepository {
    
    private final ProductPersistenceGateway gateway;
    
    public ProductRepositoryAdapter(ProductPersistenceGateway gateway) {
        this.gateway = gateway;
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return gateway.findById(id)
                .map(this::mapToProduct);
    }
    
    @Override
    public List<Product> findAll() {
        return gateway.findAll().stream()
                .map(this::mapToProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByCategory(String category) {
        return gateway.findByCategory(category).stream()
                .map(this::mapToProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findAvailableProducts() {
        return gateway.findAvailableProducts().stream()
                .map(this::mapToProduct)
                .collect(Collectors.toList());
    }
    
    @Override
    public Product save(Product product) {
        Map<String, Object> productData = mapToData(product);
        Map<String, Object> savedData = gateway.save(productData);
        return mapToProduct(savedData);
    }
    
    @Override
    public void deleteById(Long id) {
        gateway.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return gateway.existsById(id);
    }
    
    // ===============================================
    // PRIVATE MAPPING METHODS (Business Layer)
    // Convert between Map (from Gateway) and Domain Entity (Product)
    // ===============================================
    
    /**
     * Convert Map to Domain Entity (Product)
     */
    private Product mapToProduct(Map<String, Object> data) {
        if (data == null) {
            return null;
        }
        
        Product product = new Product();
        product.setId((Long) data.get("id"));
        product.setName((String) data.get("name"));
        product.setDescription((String) data.get("description"));
        product.setPrice((BigDecimal) data.get("price"));
        product.setImageUrl((String) data.get("imageUrl"));
        product.setSpecifications((String) data.get("specifications"));
        product.setCategory((String) data.get("category"));
        product.setStockQuantity((Integer) data.get("stockQuantity"));
        product.setAvailable((Boolean) data.get("available"));
        product.setCreatedAt((LocalDateTime) data.get("createdAt"));
        product.setUpdatedAt((LocalDateTime) data.get("updatedAt"));
        
        return product;
    }
    
    /**
     * Convert Domain Entity (Product) to Map
     */
    private Map<String, Object> mapToData(Product product) {
        if (product == null) {
            return null;
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", product.getId());
        data.put("name", product.getName());
        data.put("description", product.getDescription());
        data.put("price", product.getPrice());
        data.put("imageUrl", product.getImageUrl());
        data.put("specifications", product.getSpecifications());
        data.put("category", product.getCategory());
        data.put("stockQuantity", product.getStockQuantity());
        data.put("available", product.isAvailable());
        data.put("createdAt", product.getCreatedAt());
        data.put("updatedAt", product.getUpdatedAt());
        
        return data;
    }
}
