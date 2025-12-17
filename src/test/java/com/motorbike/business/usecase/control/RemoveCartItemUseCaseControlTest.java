package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.removecartitem.RemoveCartItemInputData;
import com.motorbike.business.dto.removecartitem.RemoveCartItemOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveCartItemUseCaseControlTest {

    @Test
    void shouldRemoveCartItemSuccessfully() {
        // Given
        Long userId = 1L;
        Long productId = 100L;

        GioHang cart = new GioHang(1L, userId, 0);
        cart.themSanPham(new ChiTietGioHang(productId, "Test Product", BigDecimal.valueOf(500000), 2));

        CartRepository cartRepo = new MockCartRepository(cart, true);
        RemoveCartItemUseCaseControl useCase = new RemoveCartItemUseCaseControl(null, cartRepo);

        RemoveCartItemInputData inputData = new RemoveCartItemInputData(userId, productId);

        // When
        RemoveCartItemOutputData outputData = useCase.removeInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertNotNull(outputData.getMessage());
    }

    @Test
    void shouldHandleCartNotFound() {
        // Given
        Long userId = 999L;
        Long productId = 100L;

        CartRepository cartRepo = new MockCartRepository(null, false);
        RemoveCartItemUseCaseControl useCase = new RemoveCartItemUseCaseControl(null, cartRepo);

        RemoveCartItemInputData inputData = new RemoveCartItemInputData(userId, productId);

        // When
        RemoveCartItemOutputData outputData = useCase.removeInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
    }

    @Test
    void shouldHandleNullInput() {
        // Given
        CartRepository cartRepo = new MockCartRepository(null, false);
        RemoveCartItemUseCaseControl useCase = new RemoveCartItemUseCaseControl(null, cartRepo);

        // When
        RemoveCartItemOutputData outputData = useCase.removeInternal(null);

        // Then
        assertFalse(outputData.isSuccess());
    }

    private static class MockCartRepository implements CartRepository {
        private final GioHang cart;
        private final boolean removeSuccess;

        public MockCartRepository(GioHang cart, boolean removeSuccess) {
            this.cart = cart;
            this.removeSuccess = removeSuccess;
        }

        @Override
        public Optional<GioHang> findByUserId(Long userId) {
            return Optional.ofNullable(cart);
        }

        @Override
        public GioHang save(GioHang gioHang) {
            return gioHang;
        }

        @Override
        public Optional<GioHang> findById(Long id) {
            return Optional.ofNullable(cart);
        }

        @Override
        public void deleteById(Long id) {
        }

        public List<ChiTietGioHang> findItemsByCartId(Long cartId) {
            return cart != null ? cart.getDanhSachSanPham() : new ArrayList<>();
        }

        public void deleteItemById(Long itemId) {
        }

        public Optional<ChiTietGioHang> findItemById(Long itemId) {
            return Optional.empty();
        }
        
        @Override
        public int mergeGuestCartToUserCart(Long guestCartId, Long userCartId) {
            return 0;
        }
        
        @Override
        public void delete(Long cartId) {
        }
        
        @Override
        public void deleteAllByUserId(Long userId) {
        }
        
        @Override
        public Optional<GioHang> findByUserIdAndProductId(Long userId, Long productId) {
            return Optional.empty();
        }
    }
}
