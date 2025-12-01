package com.motorbike.adapters.repositories;

import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.infrastructure.persistence.jpa.entities.DonHangJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.entities.ChiTietDonHangJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.DonHangJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepository {
    
    private final DonHangJpaRepository jpaRepository;
    
    public OrderRepositoryAdapter(DonHangJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    @Transactional
    public DonHang save(DonHang donHang) {
        DonHangJpaEntity jpaEntity = toJpaEntity(donHang);
        DonHangJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<DonHang> findById(Long orderId) {
        return jpaRepository.findById(orderId)
                .map(this::toDomain);
    }
    
    @Override
    public List<DonHang> findByUserId(Long userId) {
        return jpaRepository.findByMaTaiKhoan(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonHang> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DonHang> findByStatus(TrangThaiDonHang trangThai) {
        return jpaRepository.findByTrangThai(trangThai.name()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai) {
        return jpaRepository.findByMaTaiKhoanAndTrangThai(userId, trangThai.name()).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void deleteById(Long orderId) {
        jpaRepository.deleteById(orderId);
    }
    
    @Override
    public boolean existsById(Long orderId) {
        return jpaRepository.existsById(orderId);
    }
    
    
    
    private DonHang toDomain(DonHangJpaEntity jpaEntity) {
        List<ChiTietDonHang> items = jpaEntity.getDanhSachSanPham().stream()
                .map(this::itemToDomain)
                .collect(Collectors.toList());
        
        DonHang donHang = new DonHang(
                jpaEntity.getMaDonHang(),
                jpaEntity.getMaTaiKhoan(),
                items,
                jpaEntity.getTongTien(),
                TrangThaiDonHang.valueOf(jpaEntity.getTrangThai()),
                jpaEntity.getTenNguoiNhan(),
                jpaEntity.getSoDienThoai(),
                jpaEntity.getDiaChiGiaoHang(),
                jpaEntity.getGhiChu(),
                jpaEntity.getNgayDat(),
                jpaEntity.getNgayCapNhat()
        );
        
        return donHang;
    }
    
    
    private ChiTietDonHang itemToDomain(ChiTietDonHangJpaEntity jpaEntity) {
        return new ChiTietDonHang(
                jpaEntity.getMaChiTiet(),
                null,
                jpaEntity.getMaSanPham(),
                jpaEntity.getTenSanPham(),
                jpaEntity.getGiaBan(),
                jpaEntity.getSoLuong(),
                jpaEntity.getThanhTien()
        );
    }
    
    
    private DonHangJpaEntity toJpaEntity(DonHang donHang) {
        DonHangJpaEntity jpaEntity = new DonHangJpaEntity();
        
        jpaEntity.setMaDonHang(donHang.getMaDonHang());
        jpaEntity.setMaTaiKhoan(donHang.getMaTaiKhoan());
        jpaEntity.setTongTien(donHang.getTongTien());
        jpaEntity.setTrangThai(donHang.getTrangThai().name());
        jpaEntity.setTenNguoiNhan(donHang.getTenNguoiNhan());
        jpaEntity.setSoDienThoai(donHang.getSoDienThoai());
        jpaEntity.setDiaChiGiaoHang(donHang.getDiaChiGiaoHang());
        jpaEntity.setGhiChu(donHang.getGhiChu());
        jpaEntity.setNgayDat(donHang.getNgayDat());
        jpaEntity.setNgayCapNhat(donHang.getNgayCapNhat());
        
        for (ChiTietDonHang item : donHang.getDanhSachSanPham()) {
            ChiTietDonHangJpaEntity jpaItem = itemToJpaEntity(item);
            jpaEntity.addItem(jpaItem);
        }
        
        return jpaEntity;
    }
    
    
    private ChiTietDonHangJpaEntity itemToJpaEntity(ChiTietDonHang item) {
        ChiTietDonHangJpaEntity jpaEntity = new ChiTietDonHangJpaEntity();
        
        jpaEntity.setMaChiTiet(item.getMaChiTiet());
        jpaEntity.setMaSanPham(item.getMaSanPham());
        jpaEntity.setTenSanPham(item.getTenSanPham());
        jpaEntity.setGiaBan(item.getGiaBan());
        jpaEntity.setSoLuong(item.getSoLuong());
        jpaEntity.setThanhTien(item.getThanhTien());
        
        return jpaEntity;
    }
}
