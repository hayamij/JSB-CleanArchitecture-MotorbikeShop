package com.motorbike.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.infrastructure.persistence.jpa.entities.PhuKienXeMayJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.PhuKienXeMayJpaRepository;

public class AccessoryRepositoryImpl implements AccessoryRepository {

    private final PhuKienXeMayJpaRepository jpaRepository;

    public AccessoryRepositoryImpl(PhuKienXeMayJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PhuKienXeMay save(PhuKienXeMay accessory) {
        PhuKienXeMayJpaEntity jpaEntity = toJpaEntity(accessory);
        PhuKienXeMayJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<PhuKienXeMay> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomainEntity);
    }

    @Override
    public List<PhuKienXeMay> findAllAccessories() {
        return jpaRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private PhuKienXeMayJpaEntity toJpaEntity(PhuKienXeMay domain) {
        PhuKienXeMayJpaEntity jpa = new PhuKienXeMayJpaEntity();
        if (domain.getMaSanPham() != null) {
            jpa.setMaSanPham(domain.getMaSanPham());
        }
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

    private PhuKienXeMay toDomainEntity(PhuKienXeMayJpaEntity jpa) {
        return new PhuKienXeMay(
                jpa.getMaSanPham(),
                jpa.getTenSanPham(),
                jpa.getMoTa(),
                jpa.getGia(),
                jpa.getHinhAnh(),
                jpa.getSoLuongTonKho(),
                jpa.isConHang(),
                jpa.getNgayTao(),
                jpa.getNgayCapNhat(),
                jpa.getLoaiPhuKien(),
                jpa.getThuongHieu(),
                jpa.getChatLieu(),
                jpa.getKichThuoc()
        );
    }
}
