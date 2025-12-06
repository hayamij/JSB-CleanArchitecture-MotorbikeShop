package com.motorbike.infrastructure.persistence.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.motorbike.infrastructure.persistence.jpa.entities.DonHangJpaEntity;

@Repository
public interface DonHangJpaRepository extends JpaRepository<DonHangJpaEntity, Long> {
    
    
    @Query("SELECT d FROM DonHangJpaEntity d WHERE d.maTaiKhoan = :userId ORDER BY d.ngayDat DESC")
    List<DonHangJpaEntity> findByMaTaiKhoan(@Param("userId") Long userId);
    
    
    List<DonHangJpaEntity> findByTrangThai(String trangThai);
    
    
    @Query("SELECT d FROM DonHangJpaEntity d WHERE d.maTaiKhoan = :userId AND d.trangThai = :trangThai ORDER BY d.ngayDat DESC")
    List<DonHangJpaEntity> findByMaTaiKhoanAndTrangThai(
            @Param("userId") Long userId,
            @Param("trangThai") String trangThai);
    
    
    boolean existsByMaTaiKhoan(Long userId);
    
    
    long countByMaTaiKhoan(Long userId);
    
    
    long countByMaTaiKhoanAndTrangThai(Long userId, String trangThai);


    @Query("""
            SELECT DISTINCT d
            FROM DonHangJpaEntity d
            LEFT JOIN FETCH d.danhSachSanPham p
            WHERE LOWER(CONCAT(d.maDonHang, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(CONCAT(d.maTaiKhoan, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(d.tenNguoiNhan) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(d.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(d.diaChiGiaoHang) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(d.trangThai) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR (p.tenSanPham IS NOT NULL AND LOWER(p.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')))
            ORDER BY d.ngayDat DESC
            """)
    List<DonHangJpaEntity> searchAdminOrders(@Param("keyword") String keyword);
}
