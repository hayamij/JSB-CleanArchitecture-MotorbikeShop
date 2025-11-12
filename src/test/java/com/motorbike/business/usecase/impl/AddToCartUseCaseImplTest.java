package com.motorbike.business.usecase.impl;


import com.motorbike.domain.entities.ProductCategoryRegistry;
import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.dto.addtocart.AddToCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.AddToCartInputBoundary;
import com.motorbike.business.usecase.AddToCartOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.CartItem;
import com.motorbike.domain.entities.Product;
import com.motorbike.domain.entities.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for AddToCart Use Case
 * Tests all business rules and edge cases
 */
@DisplayName("AddToCart Use Case Tests")
class AddToCartUseCaseImplTest {

    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private AddToCartInputBoundary addToCartUseCase;
    private AddToCartViewModel viewModel;
    private AddToCartPresenter presenter;

    // Test data
    private Product testProduct;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_USER_ID = 100L;
    private static final Long TEST_CART_ID = 200L;

    @BeforeEach
    void setUp() {
        // Create mocks
        productRepository = mock(ProductRepository.class);
        cartRepository = mock(CartRepository.class);
        
        // Create real presenter and view model for integration testing
        viewModel = new AddToCartViewModel();
        presenter = new AddToCartPresenter(viewModel);
        
        // Create use case with dependencies
        addToCartUseCase = new AddToCartUseCaseImpl(presenter, productRepository, cartRepository);
        
        // Setup test product
        testProduct = new Product(
            TEST_PRODUCT_ID,
            "Honda Wave RSX",
            "Xe số tiết kiệm nhiên liệu",
            new BigDecimal("38000000"),
            "/images/wave-rsx.jpg",
            "{\"engine\":\"110cc\"}",
            ProductCategoryRegistry.motorcycle(),
            10, // stock quantity
            true, // available
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Should add product to new cart successfully")
    void testAddToCart_NewCart_Success() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        
        Cart savedCart = new Cart(TEST_USER_ID);
        savedCart.setId(TEST_CART_ID);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 2, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        verify(productRepository, times(1)).findById(TEST_PRODUCT_ID);
        verify(cartRepository, times(1)).findByUserId(TEST_USER_ID);
        verify(cartRepository, times(1)).save(any(Cart.class));
        
        assertTrue(viewModel.success);
        assertFalse(viewModel.hasError);
        assertEquals(TEST_CART_ID, viewModel.cartId);
        assertEquals(2, viewModel.addedQuantity);
        assertFalse(viewModel.itemAlreadyInCart);
        assertTrue(viewModel.showCartPopup);
        assertNotNull(viewModel.formattedTotalAmount);
    }

    @Test
    @DisplayName("Should add product to existing cart successfully")
    void testAddToCart_ExistingCart_Success() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Cart existingCart = new Cart(TEST_USER_ID);
        existingCart.setId(TEST_CART_ID);
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(existingCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 3, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertEquals(3, viewModel.addedQuantity);
        assertNotNull(viewModel.message);
        assertTrue(viewModel.message.contains("thành công"));
    }

    @Test
    @DisplayName("Should merge quantity when product already in cart")
    void testAddToCart_ProductAlreadyInCart_MergeQuantity() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Cart existingCart = new Cart(TEST_USER_ID);
        existingCart.setId(TEST_CART_ID);
        
        // Add existing item with quantity 2
        CartItem existingItem = new CartItem(
            TEST_PRODUCT_ID,
            testProduct.getName(),
            testProduct.getPrice(),
            2
        );
        existingCart.addItem(existingItem);
        
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(existingCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 3, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.itemAlreadyInCart);
        assertEquals(3, viewModel.addedQuantity);
        assertEquals(5, viewModel.newItemQuantity); // 2 + 3 = 5
        assertNotNull(viewModel.addedItemMessage);
        assertTrue(viewModel.addedItemMessage.contains("Đã cập nhật"));
    }

    @Test
    @DisplayName("Should fail when product not found")
    void testAddToCart_ProductNotFound() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.empty());
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 1, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        verify(productRepository, times(1)).findById(TEST_PRODUCT_ID);
        verify(cartRepository, never()).save(any());
        
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PRODUCT_NOT_FOUND", viewModel.errorCode);
        assertTrue(viewModel.errorMessage.contains("không tồn tại"));
    }

    @Test
    @DisplayName("Should fail when product is out of stock")
    void testAddToCart_ProductOutOfStock() {
        // Arrange
        Product outOfStockProduct = new Product(
            TEST_PRODUCT_ID,
            "Honda Wave RSX",
            "Xe số tiết kiệm nhiên liệu",
            new BigDecimal("38000000"),
            "/images/wave-rsx.jpg",
            "{\"engine\":\"110cc\"}",
            ProductCategoryRegistry.motorcycle(),
            0, // out of stock
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(outOfStockProduct));
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 1, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PRODUCT_OUT_OF_STOCK", viewModel.errorCode);
        assertEquals("ORANGE", viewModel.errorColor);
    }

    @Test
    @DisplayName("Should fail when requested quantity exceeds stock")
    void testAddToCart_InsufficientStock() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        // Request 15 but only 10 in stock
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 15, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INSUFFICIENT_STOCK", viewModel.errorCode);
        assertTrue(viewModel.errorMessage.contains("10 sản phẩm"));
    }

    @Test
    @DisplayName("Should fail when total quantity (existing + new) exceeds stock")
    void testAddToCart_TotalQuantityExceedsStock() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Cart existingCart = new Cart(TEST_USER_ID);
        existingCart.setId(TEST_CART_ID);
        
        // Already have 8 in cart
        CartItem existingItem = new CartItem(
            TEST_PRODUCT_ID,
            testProduct.getName(),
            testProduct.getPrice(),
            8
        );
        existingCart.addItem(existingItem);
        
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(existingCart));
        
        // Try to add 5 more (8 + 5 = 13 > 10 stock)
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 5, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INSUFFICIENT_STOCK", viewModel.errorCode);
        assertTrue(viewModel.errorMessage.contains("8 sản phẩm trong giỏ"));
    }

    @Test
    @DisplayName("Should fail with invalid quantity (zero)")
    void testAddToCart_InvalidQuantityZero() {
        // Arrange
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 0, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        verify(productRepository, never()).findById(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INVALID_QUANTITY", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail with invalid quantity (negative)")
    void testAddToCart_InvalidQuantityNegative() {
        // Arrange
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, -5, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INVALID_QUANTITY", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail with null product ID")
    void testAddToCart_NullProductId() {
        // Arrange
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(null, 1, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("NULL_PRODUCT_ID", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should add to guest cart successfully")
    void testAddToCart_GuestUser_Success() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Long guestCartId = 999L;
        Cart guestCart = new Cart(null); // null userId for guest
        guestCart.setId(guestCartId);
        when(cartRepository.findById(guestCartId)).thenReturn(Optional.of(guestCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(guestCart);
        
        // Guest user (userId = null, has guestCartId)
        AddToCartInputData inputData = AddToCartInputData.forGuestUser(TEST_PRODUCT_ID, 1, guestCartId);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertEquals(guestCartId, viewModel.cartId);
        verify(cartRepository).findById(guestCartId);
    }

    @Test
    @DisplayName("Should create new guest cart when no guest cart ID provided")
    void testAddToCart_NewGuestCart_Success() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Cart newGuestCart = new Cart(null);
        newGuestCart.setId(888L);
        when(cartRepository.save(any(Cart.class))).thenReturn(newGuestCart);
        
        // Guest user with no cart ID
        AddToCartInputData inputData = new AddToCartInputData(TEST_PRODUCT_ID, 2, null, null);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertEquals(888L, viewModel.cartId);
    }

    @Test
    @DisplayName("Should format price correctly")
    void testAddToCart_PriceFormatting() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        
        Cart savedCart = new Cart(TEST_USER_ID);
        savedCart.setId(TEST_CART_ID);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 1, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.formattedProductPrice);
        assertTrue(viewModel.formattedProductPrice.contains("₫"));
        assertNotNull(viewModel.formattedTotalAmount);
    }

    @Test
    @DisplayName("Should display stock status correctly")
    void testAddToCart_StockStatusDisplay() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        
        Cart savedCart = new Cart(TEST_USER_ID);
        savedCart.setId(TEST_CART_ID);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 2, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.stockStatus);
        assertEquals(10, viewModel.productStock);
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testAddToCart_PresenterCalledOnce() {
        // Arrange
        AddToCartOutputBoundary mockOutputBoundary = mock(AddToCartOutputBoundary.class);
        AddToCartInputBoundary useCase = new AddToCartUseCaseImpl(
            mockOutputBoundary, productRepository, cartRepository
        );
        
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        
        Cart savedCart = new Cart(TEST_USER_ID);
        savedCart.setId(TEST_CART_ID);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 1, TEST_USER_ID);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<AddToCartOutputData> captor = ArgumentCaptor.forClass(AddToCartOutputData.class);
        verify(mockOutputBoundary, times(1)).present(captor.capture());
        
        AddToCartOutputData capturedData = captor.getValue();
        assertTrue(capturedData.isSuccess());
        assertEquals(TEST_PRODUCT_ID, capturedData.getProductId());
    }

    @Test
    @DisplayName("Should show cart popup on success")
    void testAddToCart_ShowCartPopup() {
        // Arrange
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        
        Cart savedCart = new Cart(TEST_USER_ID);
        savedCart.setId(TEST_CART_ID);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);
        
        AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(TEST_PRODUCT_ID, 1, TEST_USER_ID);
        
        // Act
        addToCartUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.showCartPopup);
        assertNull(viewModel.redirectUrl); // Stay on current page
    }
}
