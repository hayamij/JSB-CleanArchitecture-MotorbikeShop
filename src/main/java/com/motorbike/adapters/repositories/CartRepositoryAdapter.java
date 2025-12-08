package com.motorbike.adapters.repositories;

import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.infrastructure.persistence.jpa.entities.GioHangJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.entities.ChiTietGioHangJpaEntity;
import com.motorbike.infrastructure.persistence.jpa.repositories.GioHangJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class CartRepositoryAdapter implements CartRepository {
    
    private final GioHangJpaRepository jpaRepository;
    
    public CartRepositoryAdapter(GioHangJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Optional<GioHang> findByUserId(Long userId) {
        return jpaRepository.findByUserIdWithItems(userId)
                .map(this::toDomain);
    }
    
    @Override
    public Optional<GioHang> findById(Long cartId) {
        return jpaRepository.findByIdWithItems(cartId)
                .map(this::toDomain);
    }
    
    @Override
    @Transactional
    public GioHang save(GioHang gioHang) {
        GioHangJpaEntity jpaEntity;
        
        // If cart has an ID, load the existing entity and update it
        if (gioHang.getMaGioHang() != null) {
            Optional<GioHangJpaEntity> existingOpt = jpaRepository.findByIdWithItems(gioHang.getMaGioHang());
            if (existingOpt.isPresent()) {
                jpaEntity = existingOpt.get();
                // Update existing entity
                jpaEntity.setMaTaiKhoan(gioHang.getMaTaiKhoan());
                jpaEntity.setTongTien(gioHang.getTongTien());
                
                // Clear and re-add items (orphanRemoval will handle deletion)
                // Force flush to delete orphans before adding new ones
                jpaEntity.getDanhSachSanPham().clear();
                jpaRepository.flush();
                
                for (ChiTietGioHang itemDomain : gioHang.getDanhSachSanPham()) {
                    ChiTietGioHangJpaEntity itemJpa = new ChiTietGioHangJpaEntity(
                            itemDomain.getMaSanPham(),
                            itemDomain.getTenSanPham(),
                            itemDomain.getGiaSanPham(),
                            itemDomain.getSoLuong()
                    );
                    jpaEntity.addItem(itemJpa);
                }
            } else {
                // ID exists but entity not found - create new
                jpaEntity = toJpaEntity(gioHang);
            }
        } else {
            // Create new entity (no ID means new cart)
            jpaEntity = toJpaEntity(gioHang);
        }
        
        GioHangJpaEntity saved = jpaRepository.save(jpaEntity);
        return toDomain(saved);
    }
    
    @Override
    @Transactional
    public void delete(Long cartId) {
        jpaRepository.deleteById(cartId);
    }
    
    @Override
    @Transactional
    public int mergeGuestCartToUserCart(Long guestCartId, Long userCartId) {
        Optional<GioHangJpaEntity> guestCartOpt = jpaRepository.findByIdWithItems(guestCartId);
        Optional<GioHangJpaEntity> userCartOpt = jpaRepository.findByIdWithItems(userCartId);
        
        if (guestCartOpt.isEmpty() || userCartOpt.isEmpty()) {
            return 0;
        }
        
        GioHangJpaEntity guestCart = guestCartOpt.get();
        GioHangJpaEntity userCart = userCartOpt.get();
        
        int mergedCount = 0;
        
        for (ChiTietGioHangJpaEntity guestItem : guestCart.getDanhSachSanPham()) {
            boolean found = false;
            for (ChiTietGioHangJpaEntity userItem : userCart.getDanhSachSanPham()) {
                if (userItem.getMaSanPham().equals(guestItem.getMaSanPham())) {
                    userItem.setSoLuong(userItem.getSoLuong() + guestItem.getSoLuong());
                    found = true;
                    mergedCount++;
                    break;
                }
            }
            
            if (!found) {
                ChiTietGioHangJpaEntity newItem = new ChiTietGioHangJpaEntity(
                        guestItem.getMaSanPham(),
                        guestItem.getTenSanPham(),
                        guestItem.getGiaSanPham(),
                        guestItem.getSoLuong()
                );
                userCart.addItem(newItem);
                mergedCount++;
            }
        }
        
        BigDecimal total = userCart.getDanhSachSanPham().stream()
                .map(ChiTietGioHangJpaEntity::getTamTinh)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        userCart.setTongTien(total);
        
        jpaRepository.save(userCart);
        
        jpaRepository.delete(guestCart);
        
        return mergedCount;
    }
    
    
    private GioHang toDomain(GioHangJpaEntity jpaEntity) {
        java.util.List<ChiTietGioHang> items = new java.util.ArrayList<>();
        for (ChiTietGioHangJpaEntity itemJpa : jpaEntity.getDanhSachSanPham()) {
            ChiTietGioHang item = new ChiTietGioHang(
                    itemJpa.getMaChiTiet(),
                    jpaEntity.getMaGioHang(),
                    itemJpa.getMaSanPham(),
                    itemJpa.getTenSanPham(),
                    itemJpa.getGiaSanPham(),
                    itemJpa.getSoLuong(),
                    itemJpa.getTamTinh()
            );
            items.add(item);
        }
        
        GioHang gioHang = new GioHang(
                jpaEntity.getMaGioHang(),
                jpaEntity.getMaTaiKhoan(),
                items,
                jpaEntity.getTongTien(),
                jpaEntity.getNgayTao(),
                jpaEntity.getNgayCapNhat()
        );
        
        return gioHang;
    }
    
    private GioHangJpaEntity toJpaEntity(GioHang domain) {
        GioHangJpaEntity jpa = new GioHangJpaEntity(domain.getMaTaiKhoan());
        jpa.setMaGioHang(domain.getMaGioHang());
        jpa.setTongTien(domain.getTongTien());
        
        for (ChiTietGioHang itemDomain : domain.getDanhSachSanPham()) {
            ChiTietGioHangJpaEntity itemJpa = new ChiTietGioHangJpaEntity(
                    itemDomain.getMaSanPham(),
                    itemDomain.getTenSanPham(),
                    itemDomain.getGiaSanPham(),
                    itemDomain.getSoLuong()
            );
            // NEVER set ID for new entities - let Hibernate generate it
            // Only used for new carts, so items don't have IDs yet
            jpa.addItem(itemJpa);
        }
        
        return jpa;
    }
}
