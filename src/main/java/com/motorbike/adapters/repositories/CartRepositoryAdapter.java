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

/**
 * Adapter implementation for CartRepository
 * Handles conversion between Domain and JPA entities for shopping cart
 */
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
        GioHangJpaEntity jpaEntity = toJpaEntity(gioHang);
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
        
        // Merge items from guest cart to user cart
        for (ChiTietGioHangJpaEntity guestItem : guestCart.getDanhSachSanPham()) {
            // Check if product already exists in user cart
            boolean found = false;
            for (ChiTietGioHangJpaEntity userItem : userCart.getDanhSachSanPham()) {
                if (userItem.getMaSanPham().equals(guestItem.getMaSanPham())) {
                    // Update quantity (setSoLuong automatically updates tamTinh)
                    userItem.setSoLuong(userItem.getSoLuong() + guestItem.getSoLuong());
                    found = true;
                    mergedCount++;
                    break;
                }
            }
            
            // If not found, add new item
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
        
        // Calculate total for user cart
        BigDecimal total = userCart.getDanhSachSanPham().stream()
                .map(ChiTietGioHangJpaEntity::getTamTinh)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        userCart.setTongTien(total);
        
        // Save user cart
        jpaRepository.save(userCart);
        
        // Delete guest cart
        jpaRepository.delete(guestCart);
        
        return mergedCount;
    }
    
    // Conversion methods
    
    private GioHang toDomain(GioHangJpaEntity jpaEntity) {
        // Create list of domain cart items
        java.util.List<ChiTietGioHang> items = new java.util.ArrayList<>();
        for (ChiTietGioHangJpaEntity itemJpa : jpaEntity.getDanhSachSanPham()) {
            ChiTietGioHang item = new ChiTietGioHang(
                    itemJpa.getMaChiTiet(),
                    jpaEntity.getMaGioHang(), // cart ID
                    itemJpa.getMaSanPham(),
                    itemJpa.getTenSanPham(),
                    itemJpa.getGiaSanPham(),
                    itemJpa.getSoLuong(),
                    itemJpa.getTamTinh()
            );
            items.add(item);
        }
        
        // Use full constructor to reconstruct from DB
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
        
        // Convert cart items
        for (ChiTietGioHang itemDomain : domain.getDanhSachSanPham()) {
            ChiTietGioHangJpaEntity itemJpa = new ChiTietGioHangJpaEntity(
                    itemDomain.getMaSanPham(),
                    itemDomain.getTenSanPham(),
                    itemDomain.getGiaSanPham(),
                    itemDomain.getSoLuong()
            );
            itemJpa.setMaChiTiet(itemDomain.getMaChiTiet());
            jpa.addItem(itemJpa);
        }
        
        return jpa;
    }
}
