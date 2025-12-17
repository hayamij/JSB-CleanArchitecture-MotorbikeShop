package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityInputData;
import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.GioHang;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCartItemQuantityUseCaseControlTest {

    // UpdateCartItemQuantity use case expects userId, productId, and newQuantity
    
    static class MockCartRepository implements CartRepository {
        private GioHang cart;
        
        public void setCart(GioHang cart) {
            this.cart = cart;
        }
        
        @Override
        public Optional<GioHang> findByUserId(Long userId) {
            return cart != null && cart.getMaTaiKhoan().equals(userId) ? Optional.of(cart) : Optional.empty();
        }
        
        @Override
        public GioHang save(GioHang gioHang) { return gioHang; }
        
        @Override
        public Optional<GioHang> findById(Long id) { return Optional.empty(); }
        
        @Override
        public void deleteById(Long id) {}
        
        public java.util.List<com.motorbike.domain.entities.ChiTietGioHang> findItemsByCartId(Long cartId) { return null; }
        
        public void deleteItemById(Long itemId) {}
        
        public Optional<com.motorbike.domain.entities.ChiTietGioHang> findItemById(Long itemId) { return Optional.empty(); }
        
        @Override
        public int mergeGuestCartToUserCart(Long guestUserId, Long userId) { return 0; }
        
        public void deleteByUserId(Long userId) {}
        
        @Override
        public void delete(Long cartId) {}
        
        @Override
        public void deleteAllByUserId(Long userId) {}
        
        @Override
        public Optional<GioHang> findByUserIdAndProductId(Long userId, Long productId) { return Optional.empty(); }
    }

    @Test
    void shouldUpdateCartItemQuantitySuccessfully() {
        // Given - Cart exists with product
        Long userId = 1L;
        Long productId = 100L;
        int newQuantity = 10;
        
        GioHang cart = new GioHang(userId);
        // Use 4-parameter constructor: maSanPham, tenSanPham, giaSanPham, soLuong
        cart.themSanPham(new com.motorbike.domain.entities.ChiTietGioHang(productId, "Product", java.math.BigDecimal.valueOf(1000), 5));
        
        MockCartRepository cartRepo = new MockCartRepository();
        cartRepo.setCart(cart);
        UpdateCartItemQuantityUseCaseControl useCase = new UpdateCartItemQuantityUseCaseControl(null, cartRepo);
        UpdateCartItemQuantityInputData inputData = new UpdateCartItemQuantityInputData(userId, productId, newQuantity);

        // When
        UpdateCartItemQuantityOutputData outputData = useCase.updateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(10, outputData.getNewQuantity());
        assertEquals("Cart item quantity updated successfully", outputData.getMessage());
    }

    @Test
    void shouldFailWhenCartNotFound() {
        // Given - Cart doesn't exist
        Long userId = 999L;
        Long productId = 100L;
        int newQuantity = 10;
        
        MockCartRepository cartRepo = new MockCartRepository();
        UpdateCartItemQuantityUseCaseControl useCase = new UpdateCartItemQuantityUseCaseControl(null, cartRepo);
        UpdateCartItemQuantityInputData inputData = new UpdateCartItemQuantityInputData(userId, productId, newQuantity);

        // When
        UpdateCartItemQuantityOutputData outputData = useCase.updateInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotNull(outputData.getMessage());
        assertEquals(0, outputData.getNewQuantity());
    }
}
