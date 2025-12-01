package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.PhuKienXeMayJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhuKienXeMayJpaRepository extends JpaRepository<PhuKienXeMayJpaEntity, Long> {
    
    
    List<PhuKienXeMayJpaEntity> findByLoaiPhuKien(String loaiPhuKien);
    
    
    List<PhuKienXeMayJpaEntity> findByThuongHieu(String thuongHieu);
    
    
    List<PhuKienXeMayJpaEntity> findByChatLieu(String chatLieu);
}
