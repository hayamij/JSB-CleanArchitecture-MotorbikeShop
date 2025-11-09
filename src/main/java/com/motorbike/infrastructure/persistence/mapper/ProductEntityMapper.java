package com.motorbike.infrastructure.persistence.mapper;

import com.motorbike.domain.entity.Product;
import com.motorbike.infrastructure.persistence.entity.ProductJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Domain Entity and JPA Entity
 */
@Component
public class ProductEntityMapper {
    
    /**
     * Convert from JPA Entity to Domain Entity
     * @param jpaEntity JPA entity from database
     * @return Domain entity
     */
    public Product toDomain(ProductJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        Product product = new Product();
        product.setId(jpaEntity.getId());
        product.setName(jpaEntity.getName());
        product.setDescription(jpaEntity.getDescription());
        product.setPrice(jpaEntity.getPrice());
        product.setImageUrl(jpaEntity.getImageUrl());
        product.setSpecifications(jpaEntity.getSpecifications());
        product.setCategory(jpaEntity.getCategory());
        product.setStockQuantity(jpaEntity.getStockQuantity());
        product.setAvailable(jpaEntity.isAvailable());
        product.setCreatedAt(jpaEntity.getCreatedAt());
        product.setUpdatedAt(jpaEntity.getUpdatedAt());
        
        return product;
    }
    
    /**
     * Convert from Domain Entity to JPA Entity
     * @param domain Domain entity
     * @return JPA entity for database
     */
    public ProductJpaEntity toJpaEntity(Product domain) {
        if (domain == null) {
            return null;
        }
        
        ProductJpaEntity jpaEntity = new ProductJpaEntity();
        jpaEntity.setId(domain.getId());
        jpaEntity.setName(domain.getName());
        jpaEntity.setDescription(domain.getDescription());
        jpaEntity.setPrice(domain.getPrice());
        jpaEntity.setImageUrl(domain.getImageUrl());
        jpaEntity.setSpecifications(domain.getSpecifications());
        jpaEntity.setCategory(domain.getCategory());
        jpaEntity.setStockQuantity(domain.getStockQuantity());
        jpaEntity.setAvailable(domain.isAvailable());
        jpaEntity.setCreatedAt(domain.getCreatedAt());
        jpaEntity.setUpdatedAt(domain.getUpdatedAt());
        
        return jpaEntity;
    }
}
