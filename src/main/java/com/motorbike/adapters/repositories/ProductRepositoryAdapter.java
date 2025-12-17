package com.motorbike.adapters.repositories;

import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.infrastructure.persistence.jpa.entities.SanPhamJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.entities.XeMayJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.entities.PhuKienXeMayJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.SanPhamJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductRepositoryAdapter implements ProductRepository {
    
    private final SanPhamJpaRepository jpaRepository;
    
    public ProductRepositoryAdapter(SanPhamJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<SanPham> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<SanPham> findByTenSanPham(String tenSanPham) {
        return jpaRepository.findAll().stream()
                .filter(entity -> entity.getTenSanPham().equalsIgnoreCase(tenSanPham))
                .findFirst()
                .map(this::toDomain);
    }
    
    @Override
    public Optional<SanPham> findByMaSanPham(String maSanPham) {
        try {
            Long id = Long.parseLong(maSanPham);
            return findById(id);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public SanPham save(SanPham sanPham) {
        SanPhamJpaEntity jpaEntity = toJpaEntity(sanPham);
        SanPhamJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomain(saved);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public java.util.List<SanPham> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public void deleteById(Long productId) {
        jpaRepository.deleteById(productId);
    }
    
    @Override
    public java.util.List<PhuKienXeMay> findAllAccessories() {
        return jpaRepository.findAll().stream()
                .filter(entity -> entity instanceof PhuKienXeMayJpaEntity)
                .map(entity -> (PhuKienXeMay) toDomain(entity))
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public java.util.List<PhuKienXeMay> searchAccessories(String keyword) {
        return jpaRepository.findAll().stream()
                .filter(entity -> entity instanceof PhuKienXeMayJpaEntity)
                .filter(entity -> 
                    entity.getTenSanPham().toLowerCase().contains(keyword.toLowerCase()) ||
                    (entity.getMoTa() != null && entity.getMoTa().toLowerCase().contains(keyword.toLowerCase()))
                )
                .map(entity -> (PhuKienXeMay) toDomain(entity))
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public java.util.List<XeMay> findAllMotorbikes() {
        return jpaRepository.findAll().stream()
                .filter(entity -> entity instanceof XeMayJpaEntity)
                .map(entity -> (XeMay) toDomain(entity))
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public java.util.List<XeMay> searchMotorbikes(String keyword) {
        return jpaRepository.findAll().stream()
                .filter(entity -> entity instanceof XeMayJpaEntity)
                .filter(entity -> 
                    entity.getTenSanPham().toLowerCase().contains(keyword.toLowerCase()) ||
                    (entity.getMoTa() != null && entity.getMoTa().toLowerCase().contains(keyword.toLowerCase()))
                )
                .map(entity -> (XeMay) toDomain(entity))
                .collect(java.util.stream.Collectors.toList());
    }
    
    
    private SanPham toDomain(SanPhamJpaEntity jpaEntity) {
        if (jpaEntity instanceof XeMayJpaEntity) {
            XeMayJpaEntity xeMay = (XeMayJpaEntity) jpaEntity;
            return new XeMay(
                    xeMay.getMaSanPham(),
                    xeMay.getTenSanPham(),
                    xeMay.getMoTa(),
                    xeMay.getGia(),
                    xeMay.getHinhAnh(),
                    xeMay.getSoLuongTonKho(),
                    xeMay.isConHang(),
                    xeMay.getNgayTao(),
                    xeMay.getNgayCapNhat(),
                    xeMay.getHangXe(),
                    xeMay.getDongXe(),
                    xeMay.getMauSac(),
                    xeMay.getNamSanXuat(),
                    xeMay.getDungTich()
            );
        } else if (jpaEntity instanceof PhuKienXeMayJpaEntity) {
            PhuKienXeMayJpaEntity phuKien = (PhuKienXeMayJpaEntity) jpaEntity;
            return new PhuKienXeMay(
                    phuKien.getMaSanPham(),
                    phuKien.getTenSanPham(),
                    phuKien.getMoTa(),
                    phuKien.getGia(),
                    phuKien.getHinhAnh(),
                    phuKien.getSoLuongTonKho(),
                    phuKien.isConHang(),
                    phuKien.getNgayTao(),
                    phuKien.getNgayCapNhat(),
                    phuKien.getLoaiPhuKien(),
                    phuKien.getThuongHieu(),
                    phuKien.getChatLieu(),
                    phuKien.getKichThuoc()
            );
        }
        
        throw new IllegalArgumentException("Unknown product type: " + jpaEntity.getClass());
    }
    
    private SanPhamJpaEntity toJpaEntity(SanPham domain) {
        if (domain instanceof XeMay) {
            XeMay xeMay = (XeMay) domain;
            XeMayJpaEntity jpa = new XeMayJpaEntity();
            jpa.setMaSanPham(xeMay.getMaSanPham());
            jpa.setTenSanPham(xeMay.getTenSanPham());
            jpa.setMoTa(xeMay.getMoTa());
            jpa.setGia(xeMay.getGia());
            jpa.setHinhAnh(xeMay.getHinhAnh());
            jpa.setSoLuongTonKho(xeMay.getSoLuongTonKho());
            jpa.setConHang(xeMay.isConHang());
            jpa.setHangXe(xeMay.getHangXe());
            jpa.setDongXe(xeMay.getDongXe());
            jpa.setMauSac(xeMay.getMauSac());
            jpa.setNamSanXuat(xeMay.getNamSanXuat());
            jpa.setDungTich(xeMay.getDungTich());
            return jpa;
            
        } else if (domain instanceof PhuKienXeMay) {
            PhuKienXeMay phuKien = (PhuKienXeMay) domain;
            PhuKienXeMayJpaEntity jpa = new PhuKienXeMayJpaEntity();
            jpa.setMaSanPham(phuKien.getMaSanPham());
            jpa.setTenSanPham(phuKien.getTenSanPham());
            jpa.setMoTa(phuKien.getMoTa());
            jpa.setGia(phuKien.getGia());
            jpa.setHinhAnh(phuKien.getHinhAnh());
            jpa.setSoLuongTonKho(phuKien.getSoLuongTonKho());
            jpa.setConHang(phuKien.isConHang());
            jpa.setLoaiPhuKien(phuKien.getLoaiPhuKien());
            jpa.setThuongHieu(phuKien.getThuongHieu());
            jpa.setChatLieu(phuKien.getChatLieu());
            jpa.setKichThuoc(phuKien.getKichThuoc());
            return jpa;
        }
        
        throw new IllegalArgumentException("Unknown product type: " + domain.getClass());
    }
}
