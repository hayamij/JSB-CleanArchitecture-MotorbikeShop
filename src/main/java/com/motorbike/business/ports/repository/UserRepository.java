package com.motorbike.business.ports.repository;

import java.util.List;
import java.util.Optional;

import com.motorbike.domain.entities.TaiKhoan;

public interface UserRepository {
    
    
    Optional<TaiKhoan> findByEmail(String email);
    
    
    Optional<TaiKhoan> findById(Long id);
    
    
    boolean existsByEmail(String email);
    
    
    TaiKhoan save(TaiKhoan taiKhoan);
    
    
    void updateLastLogin(Long userId);
     // Thêm: lấy tất cả người dùng (dùng cho admin)
    List<TaiKhoan> findAll();

    // Thêm: xóa người dùng theo id
    void deleteById(Long id);
}
