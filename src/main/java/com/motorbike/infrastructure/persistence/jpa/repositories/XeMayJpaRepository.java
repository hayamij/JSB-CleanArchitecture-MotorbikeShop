package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.XeMayJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for XeMayJpaEntity (Motorbike)
 * Extends SanPhamJpaRepository functionality
 */
@Repository
public interface XeMayJpaRepository extends JpaRepository<XeMayJpaEntity, Long> {
    
    /**
     * Find motorbikes by brand
     * @param hangXe Brand name
     * @return List of motorbikes
     */
    List<XeMayJpaEntity> findByHangXe(String hangXe);
    
    /**
     * Find motorbikes by model
     * @param dongXe Model name
     * @return List of motorbikes
     */
    List<XeMayJpaEntity> findByDongXe(String dongXe);
    
    /**
     * Find motorbikes by production year
     * @param namSanXuat Year
     * @return List of motorbikes
     */
    List<XeMayJpaEntity> findByNamSanXuat(int namSanXuat);
    
    /**
     * Find motorbikes by engine capacity range
     * @param minDungTich Minimum capacity
     * @param maxDungTich Maximum capacity
     * @return List of motorbikes
     */
    @Query("SELECT x FROM XeMayJpaEntity x WHERE x.dungTich BETWEEN :min AND :max")
    List<XeMayJpaEntity> findByDungTichRange(@Param("min") int minDungTich, @Param("max") int maxDungTich);
}
