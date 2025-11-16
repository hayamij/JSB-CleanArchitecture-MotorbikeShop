package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.ChiTietGioHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Update Cart Quantity Use Case Tests")
class UpdateCartQuantityUseCaseControlTest {

    private UpdateCartQuantityUseCaseControl useCase;
    private CartRepository cartRepository;
    private UpdateCartQuantityOutputBoundary outputBoundary;
    private ArgumentCaptor<UpdateCartQuantityOutputData> outputCaptor;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(UpdateCartQuantityOutputBoundary.class);
        outputCaptor = ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        useCase = new UpdateCartQuantityUseCaseControl(outputBoundary, cartRepository);
    }

    @Test
    @DisplayName("Should update quantity successfully")
    void testUpdateQuantitySuccess() {
        // Arrange
        Long cartId = 1L;
        Long userId = 1L;
        Long productId = 1L;
        int oldQuantity = 2;
        int newQuantity = 5;
        
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(cartId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), oldQuantity
        ));
        
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(GioHang.class))).thenReturn(cart);
        
        // Act
        UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(cartId, productId, newQuantity);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.isSuccess());
        assertEquals(cartId, output.getCartId());
        assertEquals(productId, output.getProductId());
        assertEquals(newQuantity, output.getNewQuantity());
        assertEquals("Honda Wave", output.getProductName());
        assertNotEquals(0, output.getTotalItems());
    }

    @Test
    @DisplayName("Should remove item when quantity is zero")
    void testRemoveItemWhenQuantityZero() {
        // Arrange
        Long cartId = 1L;
        Long userId = 1L;
        Long productId = 1L;
        
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(cartId);
        cart.themSanPham(new ChiTietGioHang(
            productId, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(GioHang.class))).thenReturn(cart);
        
        // Act
        UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(cartId, productId, 0);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(true, output.isSuccess());
        assertEquals(true, output.isItemRemoved());
        assertEquals(0, output.getNewQuantity());
        assertEquals(0, output.getTotalItems());
    }

    @Test
    @DisplayName("Should fail when cart not found")
    void testUpdateQuantityCartNotFound() {
        // Arrange
        Long cartId = 999L;
        Long productId = 1L;
        
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
        
        // Act
        UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(cartId, productId, 5);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("CART_NOT_FOUND", output.getErrorCode());
        assertNotEquals(null, output.getErrorMessage());
    }

    @Test
    @DisplayName("Should fail when product not in cart")
    void testUpdateQuantityProductNotInCart() {
        // Arrange
        Long cartId = 1L;
        Long userId = 1L;
        Long productId = 999L;
        
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(cartId);
        cart.themSanPham(new ChiTietGioHang(
            1L, "Honda Wave", new BigDecimal("30000000"), 2
        ));
        
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        
        // Act
        UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(cartId, productId, 5);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("PRODUCT_NOT_IN_CART", output.getErrorCode());
        assertNotEquals(null, output.getErrorMessage());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testUpdateQuantityFailNullInput() {
        // Act
        useCase.execute(null);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertNotEquals(null, output.getErrorCode());
        assertNotEquals(null, output.getErrorMessage());
    }

    @Test
    @DisplayName("Should fail with null cartId")
    void testUpdateQuantityFailNullCartId() {
        // Act
        UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(null, 1L, 5);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("INVALID_CART_ID", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with negative quantity")
    void testUpdateQuantityFailNegativeQuantity() {
        // Act
        UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, -5);
        useCase.execute(inputData);
        
        // Assert
        verify(outputBoundary).present(outputCaptor.capture());
        UpdateCartQuantityOutputData output = outputCaptor.getValue();
        
        assertEquals(false, output.isSuccess());
        assertEquals("INVALID_QUANTITY", output.getErrorCode());
    }
}
