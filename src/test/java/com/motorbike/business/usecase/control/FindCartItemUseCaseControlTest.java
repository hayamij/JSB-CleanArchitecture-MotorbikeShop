package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.findcartitem.FindCartItemInputData;
import com.motorbike.business.dto.findcartitem.FindCartItemOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindCartItemUseCaseControlTest {

    @Test
    void shouldFindCartItemSuccessfully() {
        // Given
        Long cartId = 1L;
        Long productId = 100L;
        
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(productId);
        
        GioHang cart = new GioHang(1L);
        cart.setMaGioHang(cartId);
        
        ChiTietGioHang cartItem = new ChiTietGioHang(cartId, productId, 5);
        cartItem.setSanPham(product);
        cart.themSanPham(cartItem);

        CartRepository cartRepo = new MockCartRepository(cart);
        FindCartItemUseCaseControl useCase = new FindCartItemUseCaseControl(null, cartRepo);

        FindCartItemInputData inputData = new FindCartItemInputData(cartId, productId);

        // When
        FindCartItemOutputData outputData = useCase.findInternal(inputData);

        // Then
        assertTrue(outputData.isFound());
    }

    @Test
    void shouldHandleCartItemNotFound() {
        // Given
        Long cartId = 1L;
        Long productId = 999L;
        
        GioHang cart = new GioHang(1L);
        cart.setMaGioHang(cartId);

        CartRepository cartRepo = new MockCartRepository(cart);
        FindCartItemUseCaseControl useCase = new FindCartItemUseCaseControl(null, cartRepo);

        FindCartItemInputData inputData = new FindCartItemInputData(cartId, productId);

        // When
        FindCartItemOutputData outputData = useCase.findInternal(inputData);

        // Then
        assertFalse(outputData.isFound());
    }

    @Test
    void shouldHandleCartNotFound() {
        // Given
        CartRepository cartRepo = new MockCartRepository(null);
        FindCartItemUseCaseControl useCase = new FindCartItemUseCaseControl(null, cartRepo);

        FindCartItemInputData inputData = new FindCartItemInputData(999L, 100L);

        // When
        FindCartItemOutputData outputData = useCase.findInternal(inputData);

        // Then
        assertFalse(outputData.isFound());
    }

    private static class MockCartRepository implements CartRepository {
        private final GioHang cart;

        public MockCartRepository(GioHang cart) {
            this.cart = cart;
        }

        @Override
        public Optional<GioHang> findByUserId(Long userId) {
            return Optional.empty();
        }

        @Override
        public GioHang save(GioHang gioHang) {
            return gioHang;
        }

        @Override
        public Optional<GioHang> findById(Long id) {
            if (cart != null && cart.getMaGioHang().equals(id)) {
                return Optional.of(cart);
            }
            return Optional.empty();
        }

        @Override
        public void delete(Long cartId) {
        }

        @Override
        public int mergeGuestCartToUserCart(Long guestCartId, Long userCartId) {
            return 0;
        }

        @Override
        public void deleteAllByUserId(Long userId) {
        }

        @Override
        public Optional<GioHang> findByUserIdAndProductId(Long userId, Long productId) {
            return Optional.empty();
        }

        @Override
        public void deleteById(Long cartId) {
        }
    }
}
