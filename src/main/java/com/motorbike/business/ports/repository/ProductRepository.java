package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.SanPham;
import java.util.Optional;

public interface ProductRepository {
    
    
    Optional<SanPham> findById(Long productId);
    
    
    SanPham save(SanPham sanPham);
    
    
    boolean existsById(Long productId);
    
    
    java.util.List<SanPham> findAll();
}
