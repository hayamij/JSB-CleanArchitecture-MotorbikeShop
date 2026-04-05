package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.clearcart.ClearCartInputData;
import com.motorbike.business.dto.clearcart.ClearCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.domain.entities.GioHang;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearCartUseCaseControlTest {

    @Test
    void shouldClearCartSuccessfully() {
        // Given
        Long cartId = 1L;
        GioHang cart = new GioHang(100L);
        cart.setMaGioHang(cartId);

        CartRepository cartRepo = new MockCartRepository(cart);
        ClearCartUseCaseControl useCase = new ClearCartUseCaseControl(null, cartRepo);

        ClearCartInputData inputData = new ClearCartInputData(cartId);

        // When
        ClearCartOutputData outputData = useCase.clearInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(cartId, outputData.getCartId());
    }

    @Test
    void shouldFailWhenCartNotFound() {
        // Given
        CartRepository cartRepo = new MockCartRepository(null);
        ClearCartUseCaseControl useCase = new ClearCartUseCaseControl(null, cartRepo);

        ClearCartInputData inputData = new ClearCartInputData(999L);

        // When
        ClearCartOutputData outputData = useCase.clearInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
    }

    private static class MockCartRepository implements CartRepository {
        private GioHang cart;

        public MockCartRepository(GioHang cart) {
            this.cart = cart;
        }

        @Override
        public Optional<GioHang> findByUserId(Long userId) {
            return Optional.empty();
        }

        @Override
        public GioHang save(GioHang gioHang) {
            this.cart = gioHang;
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
