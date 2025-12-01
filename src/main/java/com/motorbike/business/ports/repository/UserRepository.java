package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.TaiKhoan;
import java.util.Optional;

public interface UserRepository {
    
    
    Optional<TaiKhoan> findByEmail(String email);
    
    
    Optional<TaiKhoan> findById(Long id);
    
    
    boolean existsByEmail(String email);
    
    
    TaiKhoan save(TaiKhoan taiKhoan);
    
    
    void updateLastLogin(Long userId);
}
