package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.TaiKhoan;
import java.util.Optional;
import java.util.List;

public interface UserRepository {
    
    
    Optional<TaiKhoan> findByEmail(String email);
    
    
    Optional<TaiKhoan> findByUsernameOrEmailOrPhone(String username);
    
    
    Optional<TaiKhoan> findById(Long id);
    
    
    boolean existsByEmail(String email);
    
    
    boolean existsByUsername(String username);
    
    
    TaiKhoan save(TaiKhoan taiKhoan);
    
    
    void updateLastLogin(Long userId);
    
    
    List<TaiKhoan> findAll();
    
    
    void deleteById(Long userId);
    
    
    boolean existsById(Long userId);
    
    
    List<TaiKhoan> searchUsers(String keyword);
}
