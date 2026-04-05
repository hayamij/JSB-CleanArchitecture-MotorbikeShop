package com.motorbike.business.ports.repository;

import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;
import java.util.Optional;
import java.util.List;

public interface ProductRepository {
    
    
    Optional<SanPham> findById(Long productId);
    
    Optional<SanPham> findByMaSanPham(String maSanPham);
    
    Optional<SanPham> findByTenSanPham(String tenSanPham);
    
    SanPham save(SanPham sanPham);
    
    
    boolean existsById(Long productId);
    
    
    List<SanPham> findAll();
    
    
    void deleteById(Long productId);
    
    
    List<PhuKienXeMay> findAllAccessories();
    
    
    List<PhuKienXeMay> searchAccessories(String keyword);

    List<XeMay> findAllMotorbikes();

    List<XeMay> searchMotorbikes(String keyword);

}
