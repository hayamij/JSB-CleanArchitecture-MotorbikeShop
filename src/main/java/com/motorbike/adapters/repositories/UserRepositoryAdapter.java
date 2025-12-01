package com.motorbike.adapters.repositories;

import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.infrastructure.persistence.jpa.entities.TaiKhoanJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.TaiKhoanJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepository {
    
    private final TaiKhoanJpaRepository jpaRepository;
    
    public UserRepositoryAdapter(TaiKhoanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<TaiKhoan> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<TaiKhoan> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }
    
    @Override
    public TaiKhoan save(TaiKhoan taiKhoan) {
        TaiKhoanJpaEntity jpaEntity = toJpaEntity(taiKhoan);
        TaiKhoanJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomain(saved);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
    
    @Override
    public void updateLastLogin(Long userId) {
        jpaRepository.findById(userId).ifPresent(user -> {
            user.setLanDangNhapCuoi(java.time.LocalDateTime.now());
            jpaRepository.save(user);
        });
    }
    
    
    private TaiKhoan toDomain(TaiKhoanJpaEntity jpaEntity) {
        return new TaiKhoan(
                jpaEntity.getMaTaiKhoan(),
                jpaEntity.getEmail(),
                jpaEntity.getTenDangNhap(),
                jpaEntity.getMatKhau(),
                jpaEntity.getSoDienThoai(),
                jpaEntity.getDiaChi(),
                convertToVaiTro(jpaEntity.getVaiTro()),
                jpaEntity.isHoatDong(),
                jpaEntity.getNgayTao(),
                jpaEntity.getNgayCapNhat(),
                jpaEntity.getLanDangNhapCuoi()
        );
    }
    
    private TaiKhoanJpaEntity toJpaEntity(TaiKhoan domain) {
        TaiKhoanJpaEntity jpa = new TaiKhoanJpaEntity();
        jpa.setMaTaiKhoan(domain.getMaTaiKhoan());
        jpa.setEmail(domain.getEmail());
        jpa.setTenDangNhap(domain.getTenDangNhap());
        jpa.setMatKhau(domain.getMatKhau());
        jpa.setSoDienThoai(domain.getSoDienThoai());
        jpa.setDiaChi(domain.getDiaChi());
        jpa.setVaiTro(convertToJpaEnum(domain.getVaiTro()));
        jpa.setHoatDong(domain.isHoatDong());
        jpa.setNgayTao(domain.getNgayTao());
        jpa.setNgayCapNhat(domain.getNgayCapNhat());
        jpa.setLanDangNhapCuoi(domain.getLanDangNhapCuoi());
        return jpa;
    }
    
    private VaiTro convertToVaiTro(TaiKhoanJpaEntity.VaiTroEnum jpaEnum) {
        return VaiTro.valueOf(jpaEnum.name());
    }
    
    private TaiKhoanJpaEntity.VaiTroEnum convertToJpaEnum(VaiTro domain) {
        return TaiKhoanJpaEntity.VaiTroEnum.valueOf(domain.name());
    }
}
