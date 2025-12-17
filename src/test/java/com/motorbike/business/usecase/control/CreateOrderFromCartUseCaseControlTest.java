package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.createorder.CreateOrderFromCartInputData;
import com.motorbike.business.dto.createorder.CreateOrderFromCartOutputData;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateOrderFromCartUseCaseControlTest {

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        Long userId = 1L;
        
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(1L);
        
        ChiTietGioHang cartItem = new ChiTietGioHang(1L, 1L, 2);
        cartItem.setSanPham(product);
        cart.themSanPham(cartItem);

        CreateOrderFromCartUseCaseControl useCase = new CreateOrderFromCartUseCaseControl(null);

        CreateOrderFromCartInputData inputData = new CreateOrderFromCartInputData(
            cart, "Test User", "0912345678", "123 Main St", "Test note", null
        );

        // When
        CreateOrderFromCartOutputData outputData = useCase.createOrderInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(userId, outputData.getUserId());
        assertNotEquals(null, outputData.getTotalAmount());
        assertTrue(outputData.getTotalItems() > 0);
    }

    @Test
    void shouldFailWhenCartIsNull() {
        // Given
        CreateOrderFromCartUseCaseControl useCase = new CreateOrderFromCartUseCaseControl(null);

        CreateOrderFromCartInputData inputData = new CreateOrderFromCartInputData(
            null, "Test User", "0912345678", "123 Main St", "Test note", null
        );

        // When
        CreateOrderFromCartOutputData outputData = useCase.createOrderInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotEquals(null, outputData.getErrorCode());
    }

    @Test
    void shouldFailWhenReceiverNameIsEmpty() {
        // Given
        Long userId = 1L;
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(1L);

        CreateOrderFromCartUseCaseControl useCase = new CreateOrderFromCartUseCaseControl(null);

        CreateOrderFromCartInputData inputData = new CreateOrderFromCartInputData(
            cart, "", "0912345678", "123 Main St", "Test note", null
        );

        // When
        CreateOrderFromCartOutputData outputData = useCase.createOrderInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
    }
}
