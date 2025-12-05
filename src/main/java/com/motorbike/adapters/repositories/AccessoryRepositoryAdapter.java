package com.motorbike.adapters.repositories;

import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.infrastructure.persistence.jpa.entities.PhuKienXeMayJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.PhuKienXeMayJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccessoryRepositoryAdapter implements AccessoryRepository {
    
    private final PhuKienXeMayJpaRepository jpaRepository;
    
    public AccessoryRepositoryAdapter(PhuKienXeMayJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public List<PhuKienXeMay> findAllAccessories() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<PhuKienXeMay> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public PhuKienXeMay save(PhuKienXeMay phuKien) {
        PhuKienXeMayJpaEntity jpaEntity = toJpaEntity(phuKien);
        PhuKienXeMayJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomain(saved);
    }
    
    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
    
    @Override
    public List<PhuKienXeMay> searchAccessories(String keyword) {
        // Tìm kiếm theo tên sản phẩm hoặc mô tả chứa keyword
        return jpaRepository.findAll().stream()
                .filter(entity -> 
                    entity.getTenSanPham().toLowerCase().contains(keyword.toLowerCase()) ||
                    (entity.getMoTa() != null && entity.getMoTa().toLowerCase().contains(keyword.toLowerCase()))
                )
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PhuKienXeMay> search(String keyword, String loaiPhuKien, String thuongHieu) {
        return jpaRepository.findAll().stream()
                .filter(entity -> {
                    boolean matches = true;
                    
                    // Lọc theo keyword (tên hoặc mô tả)
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        String lowerKeyword = keyword.toLowerCase();
                        matches = entity.getTenSanPham().toLowerCase().contains(lowerKeyword) ||
                                (entity.getMoTa() != null && entity.getMoTa().toLowerCase().contains(lowerKeyword));
                    }
                    
                    // Lọc theo loại phụ kiện
                    if (matches && loaiPhuKien != null && !loaiPhuKien.trim().isEmpty()) {
                        matches = entity.getLoaiPhuKien().equalsIgnoreCase(loaiPhuKien);
                    }
                    
                    // Lọc theo thương hiệu
                    if (matches && thuongHieu != null && !thuongHieu.trim().isEmpty()) {
                        matches = entity.getThuongHieu().equalsIgnoreCase(thuongHieu);
                    }
                    
                    return matches;
                })
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    private PhuKienXeMay toDomain(PhuKienXeMayJpaEntity jpaEntity) {
        return new PhuKienXeMay(
                jpaEntity.getMaSanPham(),
                jpaEntity.getTenSanPham(),
                jpaEntity.getMoTa(),
                jpaEntity.getGia(),
                jpaEntity.getHinhAnh(),
                jpaEntity.getSoLuongTonKho(),
                jpaEntity.isConHang(),
                jpaEntity.getNgayTao(),
                jpaEntity.getNgayCapNhat(),
                jpaEntity.getLoaiPhuKien(),
                jpaEntity.getThuongHieu(),
                jpaEntity.getChatLieu(),
                jpaEntity.getKichThuoc()
        );
    }
    
    private PhuKienXeMayJpaEntity toJpaEntity(PhuKienXeMay domain) {
        PhuKienXeMayJpaEntity jpa = new PhuKienXeMayJpaEntity();
        jpa.setMaSanPham(domain.getMaSanPham());
        jpa.setTenSanPham(domain.getTenSanPham());
        jpa.setMoTa(domain.getMoTa());
        jpa.setGia(domain.getGia());
        jpa.setHinhAnh(domain.getHinhAnh());
        jpa.setSoLuongTonKho(domain.getSoLuongTonKho());
        jpa.setConHang(domain.isConHang());
        jpa.setLoaiPhuKien(domain.getLoaiPhuKien());
        jpa.setThuongHieu(domain.getThuongHieu());
        jpa.setChatLieu(domain.getChatLieu());
        jpa.setKichThuoc(domain.getKichThuoc());
        return jpa;
    }
}
