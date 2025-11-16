package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Checkout Use Case Tests")
class CheckoutUseCaseControlTest {

    private CheckoutUseCaseControl useCase;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private CheckoutOutputBoundary outputBoundary;
    private ArgumentCaptor<CheckoutOutputData> outputCaptor;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        cartRepository = mock(CartRepository.class);
        orderRepository = mock(OrderRepository.class);
        outputBoundary = mock(CheckoutOutputBoundary.class);
        outputCaptor = ArgumentCaptor.forClass(CheckoutOutputData.class);
        useCase = new CheckoutUseCaseControl(outputBoundary, cartRepository, productRepository, orderRepository);
    }

    @Test
    @DisplayName("Should checkout successfully")
    void testCheckoutSuccess() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số",
            new BigDecimal("30000000"), "wave.jpg", 10, true,
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave", "Đỏ", 2023, 110
        );
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(orderRepository.save(any(DonHang.class))).thenAnswer(invocation -> {
            DonHang order = invocation.getArgument(0);
            order.setMaDonHang(1L);
            return order;
        });
        
        // Act
        CheckoutInputData inputData = new CheckoutInputData(userId, "Nguyen Van A", "0123456789", "123 Test St", "Test note");
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        CheckoutOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.isSuccess());
        assertNotEquals(null, output.getOrderId());
        assertNotEquals(null, output.getTotalAmount());
    }

    @Test
    @DisplayName("Should fail with empty cart")
    void testCheckoutFailEmptyCart() {
        // Arrange
        Long userId = 1L;
        GioHang cart = new GioHang(userId);
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        
        // Act
        CheckoutInputData inputData = new CheckoutInputData(userId, "Nguyen Van A", "0123456789", "123 Test St", "Test note");
        useCase.execute(inputData);
        
        // Assert
        verify(productRepository, never()).save(any());
        verify(outputBoundary).present(any(CheckoutOutputData.class));
    }

    @Test
    @DisplayName("Should fail with insufficient stock")
    void testCheckoutFailInsufficientStock() {
        // Arrange
        Long userId = 1L;
        Long productId = 1L;
        
        GioHang cart = new GioHang(userId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 10
        ));
        
        XeMay product = new XeMay(
            productId, "Honda Wave", "Xe số",
            new BigDecimal("30000000"), "wave.jpg", 5, true, // Only 5 in stock
            java.time.LocalDateTime.now(), java.time.LocalDateTime.now(),
            "Honda", "Wave", "Đỏ", 2023, 110
        );
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        CheckoutInputData inputData = new CheckoutInputData(userId, "Nguyen Van A", "0123456789", "123 Test St", "Test note");
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        CheckoutOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("INSUFFICIENT_STOCK", output.getErrorCode());
        assertNotEquals(null, output.getErrorMessage());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testCheckoutFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        CheckoutOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertNotEquals(null, output.getErrorCode());
    }
}
