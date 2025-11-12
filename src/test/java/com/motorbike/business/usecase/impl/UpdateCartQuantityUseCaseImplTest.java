package com.motorbike.business.usecase.impl;


import com.motorbike.domain.entities.ProductCategoryRegistry;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.CartItem;
import com.motorbike.domain.entities.Product;
import com.motorbike.domain.entities.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UpdateCartQuantityUseCaseImpl
 * 
 * Test Coverage:
 * - Update quantity successfully (logged-in user)
 * - Update quantity successfully (guest user)
 * - Quantity 0 removes item from cart
 * - Negative quantity validation
 * - Cart not found error
 * - Product not in cart error
 * - Product not found error
 * - Product not available error
 * - Insufficient stock validation
 * - Cart saved after update
 * - Output data correctness
 * - Presenter called once
 */
@DisplayName("UpdateCartQuantity Use Case Tests")
class UpdateCartQuantityUseCaseImplTest {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UpdateCartQuantityOutputBoundary outputBoundary;
    private UpdateCartQuantityUseCaseImpl useCase;

    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);
        outputBoundary = mock(UpdateCartQuantityOutputBoundary.class);
        
        useCase = new UpdateCartQuantityUseCaseImpl(
            cartRepository,
            productRepository,
            outputBoundary
        );

        // Setup test product
        testProduct = new Product(
            1L,
            "Yamaha Exciter 155",
            "Sport bike",
            new BigDecimal("50000000"),
            "image.jpg",
            "{\"engine\": \"155cc\"}",
            ProductCategoryRegistry.motorcycle(),
            20, // stock quantity
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Setup test cart item
        testCartItem = new CartItem(
            1L,
            1L, // cartId
            1L, // productId
            "Yamaha Exciter 155",
            new BigDecimal("50000000"),
            2,  // quantity
            new BigDecimal("100000000") // subtotal
        );

        // Setup test cart
        List<CartItem> items = new ArrayList<>();
        items.add(testCartItem);
        testCart = new Cart(
            1L,
            100L, // userId
            items,
            new BigDecimal("100000000"), // total
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Should update quantity successfully for logged-in user")
    void testUpdateQuantity_LoggedInUser_Success() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 3);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertTrue(outputData.isSuccess());
        assertEquals(1L, outputData.getProductId());
        assertEquals("Yamaha Exciter 155", outputData.getProductName());
        assertEquals(2, outputData.getOldQuantity());
        assertEquals(3, outputData.getNewQuantity());
        assertFalse(outputData.isItemRemoved());
        
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should update quantity successfully for guest user")
    void testUpdateQuantity_GuestUser_Success() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forGuestUser(1L, 1L, 5);
        
        when(cartRepository.findById(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getOldQuantity());
        assertEquals(5, outputData.getNewQuantity());
        
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should remove item when quantity is 0")
    void testUpdateQuantity_QuantityZero_RemovesItem() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 0);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertTrue(outputData.isSuccess());
        assertEquals(1L, outputData.getProductId());
        assertEquals(2, outputData.getOldQuantity());
        assertEquals(0, outputData.getNewQuantity());
        assertTrue(outputData.isItemRemoved());
        assertNull(outputData.getItemSubtotal());
        
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when quantity is negative")
    void testUpdateQuantity_NegativeQuantity_Error() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, -1);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("INVALID_QUANTITY", outputData.getErrorCode());
        assertEquals("Quantity cannot be negative", outputData.getErrorMessage());
        
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when input data is null")
    void testUpdateQuantity_NullInput_Error() {
        // Act
        useCase.execute(null);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("INVALID_INPUT", outputData.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when cart not found")
    void testUpdateQuantity_CartNotFound_Error() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(999L, 1L, 3);
        
        when(cartRepository.findByUserId(999L)).thenReturn(Optional.empty());

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("CART_NOT_FOUND", outputData.getErrorCode());
        
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when product not in cart")
    void testUpdateQuantity_ProductNotInCart_Error() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 999L, 3);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("PRODUCT_NOT_IN_CART", outputData.getErrorCode());
        
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when product not found in database")
    void testUpdateQuantity_ProductNotFound_Error() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 3);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("PRODUCT_NOT_FOUND", outputData.getErrorCode());
        
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when product not available")
    void testUpdateQuantity_ProductNotAvailable_Error() {
        // Arrange
        testProduct.setAvailable(false);
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 3);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("PRODUCT_NOT_AVAILABLE", outputData.getErrorCode());
        
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should fail when insufficient stock")
    void testUpdateQuantity_InsufficientStock_Error() {
        // Arrange
        testProduct.setStockQuantity(2); // Only 2 in stock
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 10); // Want 10
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertFalse(outputData.isSuccess());
        assertEquals("INSUFFICIENT_STOCK", outputData.getErrorCode());
        assertTrue(outputData.getErrorMessage().contains("Available: 2"));
        
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should save cart after successful update")
    void testUpdateQuantity_SavesCart() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 4);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<Cart> cartCaptor = ArgumentCaptor.forClass(Cart.class);
        verify(cartRepository, times(1)).save(cartCaptor.capture());
        
        Cart savedCart = cartCaptor.getValue();
        assertEquals(1L, savedCart.getId());
        assertEquals(100L, savedCart.getUserId());
    }

    @Test
    @DisplayName("Should return correct output data structure")
    void testUpdateQuantity_CorrectOutputData() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 3);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertTrue(outputData.isSuccess());
        assertEquals(1L, outputData.getCartId());
        assertEquals(100L, outputData.getUserId());
        assertEquals(1L, outputData.getProductId());
        assertEquals("Yamaha Exciter 155", outputData.getProductName());
        assertEquals(2, outputData.getOldQuantity());
        assertEquals(3, outputData.getNewQuantity());
        assertFalse(outputData.isItemRemoved());
        assertNotNull(outputData.getTotalAmount());
        assertNotNull(outputData.getItemSubtotal());
        assertNotNull(outputData.getAllItems());
        assertFalse(outputData.getAllItems().isEmpty());
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testUpdateQuantity_PresenterCalledOnce() {
        // Arrange
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 3);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        verify(outputBoundary, times(1)).present(any(UpdateCartQuantityOutputData.class));
    }

    @Test
    @DisplayName("Should update to maximum available stock successfully")
    void testUpdateQuantity_MaximumStock_Success() {
        // Arrange
        testProduct.setStockQuantity(10);
        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 10); // Exactly max stock
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertTrue(outputData.isSuccess());
        assertEquals(10, outputData.getNewQuantity());
        
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Should handle cart with multiple items correctly")
    void testUpdateQuantity_MultipleItems_Success() {
        // Arrange
        CartItem secondItem = new CartItem(
            2L,
            1L, // same cartId
            2L, // different productId
            "Honda Winner X",
            new BigDecimal("45000000"),
            1,
            new BigDecimal("45000000") // subtotal
        );
        testCart.addItem(secondItem);

        Product secondProduct = new Product(
            2L,
            "Honda Winner X",
            "Sport bike",
            new BigDecimal("45000000"),
            "image2.jpg",
            "{\"engine\": \"150cc\"}",
            ProductCategoryRegistry.motorcycle(),
            15,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        UpdateCartQuantityInputData inputData = 
            UpdateCartQuantityInputData.forLoggedInUser(100L, 1L, 5);
        
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.findById(2L)).thenReturn(Optional.of(secondProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<UpdateCartQuantityOutputData> captor = 
            ArgumentCaptor.forClass(UpdateCartQuantityOutputData.class);
        verify(outputBoundary, times(1)).present(captor.capture());
        
        UpdateCartQuantityOutputData outputData = captor.getValue();
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getAllItems().size()); // Should have both items
        assertEquals(2, outputData.getTotalItems()); // 2 different products
    }
}
