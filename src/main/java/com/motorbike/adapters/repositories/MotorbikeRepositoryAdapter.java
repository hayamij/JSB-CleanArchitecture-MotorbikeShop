package com.motorbike.adapters.repositories;

import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.infrastructure.persistence.jpa.entities.XeMayJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.XeMayJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MotorbikeRepositoryAdapter implements MotorbikeRepository {

    private final XeMayJpaRepository xeMayJpaRepository;

    public MotorbikeRepositoryAdapter(XeMayJpaRepository xeMayJpaRepository) {
        this.xeMayJpaRepository = xeMayJpaRepository;
    }

    @Override
    public List<XeMay> findAllMotorbikes() {
        return xeMayJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<XeMay> findById(Long id) {
        return xeMayJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public XeMay save(XeMay xeMay) {
        XeMayJpaEntity entity = toJpaEntity(xeMay);
        XeMayJpaEntity saved = xeMayJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        xeMayJpaRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return xeMayJpaRepository.existsById(id);
    }
    
    @Override
    public List<XeMay> searchMotorbikes(String keyword) {
        return xeMayJpaRepository.findAll().stream()
                .filter(entity -> 
                    entity.getTenSanPham().toLowerCase().contains(keyword.toLowerCase()) ||
                    (entity.getMoTa() != null && entity.getMoTa().toLowerCase().contains(keyword.toLowerCase())) ||
                    entity.getHangXe().toLowerCase().contains(keyword.toLowerCase()) ||
                    entity.getDongXe().toLowerCase().contains(keyword.toLowerCase())
                )
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<XeMay> saveAll(List<XeMay> xeMayList) {
        List<XeMayJpaEntity> entities = xeMayList.stream()
                .map(this::toJpaEntity)
                .collect(Collectors.toList());
        
        List<XeMayJpaEntity> savedEntities = xeMayJpaRepository.saveAll(entities);
        
        return savedEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ================= MAPPER =================

    private XeMay toDomain(XeMayJpaEntity e) {
        return new XeMay(
                e.getMaSanPham(),
                e.getTenSanPham(),
                e.getMoTa(),
                e.getGia(),
                e.getHinhAnh(),
                e.getSoLuongTonKho(),
                e.isConHang(),
                e.getNgayTao(),
                e.getNgayCapNhat(),
                e.getHangXe(),
                e.getDongXe(),
                e.getMauSac(),
                e.getNamSanXuat(),
                e.getDungTich()
        );
    }

    private XeMayJpaEntity toJpaEntity(XeMay x) {
        XeMayJpaEntity e = new XeMayJpaEntity();
        e.setMaSanPham(x.getMaSanPham());
        e.setTenSanPham(x.getTenSanPham());
        e.setMoTa(x.getMoTa());
        e.setGia(x.getGia());
        e.setHinhAnh(x.getHinhAnh());
        e.setSoLuongTonKho(x.getSoLuongTonKho());
        e.setConHang(x.isConHang());
        e.setHangXe(x.getHangXe());
        e.setDongXe(x.getDongXe());
        e.setMauSac(x.getMauSac());
        e.setNamSanXuat(x.getNamSanXuat());
        e.setDungTich(x.getDungTich());

        e.setLoaiSanPham("XE_MAY");
        return e;
    }
}
