package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.PhuKienXeMayJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for PhuKienXeMayJpaEntity (Accessory)
 * Extends SanPhamJpaRepository functionality
 */
@Repository
public interface PhuKienXeMayJpaRepository extends JpaRepository<PhuKienXeMayJpaEntity, Long> {
    
    /**
     * Find accessories by type
     * @param loaiPhuKien Accessory type
     * @return List of accessories
     */
    List<PhuKienXeMayJpaEntity> findByLoaiPhuKien(String loaiPhuKien);
    
    /**
     * Find accessories by brand
     * @param thuongHieu Brand name
     * @return List of accessories
     */
    List<PhuKienXeMayJpaEntity> findByThuongHieu(String thuongHieu);
    
    /**
     * Find accessories by material
     * @param chatLieu Material
     * @return List of accessories
     */
    List<PhuKienXeMayJpaEntity> findByChatLieu(String chatLieu);
}
