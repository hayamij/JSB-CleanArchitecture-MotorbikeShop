package com.motorbike.infrastructure.persistence.adapter;

import com.motorbike.domain.entity.Product;
import com.motorbike.domain.repository.ProductRepository;
import com.motorbike.infrastructure.persistence.entity.ProductJpaEntity;
import com.motorbike.infrastructure.persistence.mapper.ProductEntityMapper;
import com.motorbike.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementation of ProductRepository
 * Bridges the domain repository interface with Spring Data JPA
 */
@Component
public class ProductRepositoryAdapter implements ProductRepository {
    
    private final ProductJpaRepository jpaRepository;
    private final ProductEntityMapper mapper;
    
    public ProductRepositoryAdapter(ProductJpaRepository jpaRepository, 
                                   ProductEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByCategory(String category) {
        return jpaRepository.findByCategory(category).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findAvailableProducts() {
        return jpaRepository.findAvailableProducts().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Product save(Product product) {
        ProductJpaEntity jpaEntity = mapper.toJpaEntity(product);
        ProductJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
