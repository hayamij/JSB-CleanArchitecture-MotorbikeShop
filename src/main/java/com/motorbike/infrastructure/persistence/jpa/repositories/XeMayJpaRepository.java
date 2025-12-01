package com.motorbike.infrastructure.persistence.jpa.repositories;

import com.motorbike.infrastructure.persistence.jpa.entities.XeMayJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XeMayJpaRepository extends JpaRepository<XeMayJpaEntity, Long> {
    
    
    List<XeMayJpaEntity> findByHangXe(String hangXe);
    
    
    List<XeMayJpaEntity> findByDongXe(String dongXe);
    
    
    List<XeMayJpaEntity> findByNamSanXuat(int namSanXuat);
    
    
    @Query("SELECT x FROM XeMayJpaEntity x WHERE x.dungTich BETWEEN :min AND :max")
    List<XeMayJpaEntity> findByDungTichRange(@Param("min") int minDungTich, @Param("max") int maxDungTich);
}
