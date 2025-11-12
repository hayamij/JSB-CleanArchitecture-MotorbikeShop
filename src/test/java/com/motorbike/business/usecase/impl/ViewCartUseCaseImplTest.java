package com.motorbike.business.usecase.impl;


import com.motorbike.domain.entities.ProductCategoryRegistry;
import com.motorbike.adapters.presenters.ViewCartPresenter;
import com.motorbike.adapters.viewmodels.ViewCartViewModel;
import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.dto.viewcart.ViewCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.ViewCartInputBoundary;
import com.motorbike.business.usecase.ViewCartOutputBoundary;
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

@DisplayName("ViewCart Use Case Tests")
class ViewCartUseCaseImplTest {
    
    private ViewCartInputBoundary viewCartUseCase;
    private ViewCartPresenter presenter;
    private ViewCartViewModel viewModel;
    private CartRepository cartRepository;
    private ProductRepository productRepository;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_CART_ID = 100L;
    private static final Long TEST_GUEST_CART_ID = 200L;
    private static final Long TEST_PRODUCT_ID = 1L;

    private Cart testCart;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);
        
        presenter = new ViewCartPresenter();
        viewCartUseCase = new ViewCartUseCaseImpl(presenter, cartRepository, productRepository);

        // Setup test product
        testProduct = new Product(
            TEST_PRODUCT_ID,
            "Honda Wave RSX",
            "Xe số tiết kiệm nhiên liệu",
            new BigDecimal("38000000"),
            "/images/wave-rsx.jpg",
            "{\"engine\":\"110cc\"}",
            ProductCategoryRegistry.motorcycle(),
            10,
            true,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // Setup test cart with items
        testCart = new Cart(TEST_USER_ID);
        testCart.setId(TEST_CART_ID);
        CartItem cartItem = new CartItem(
            TEST_PRODUCT_ID,
            testProduct.getName(),
            testProduct.getPrice(),
            2
        );
        testCart.addItem(cartItem);
    }

    @Test
    @DisplayName("Should view cart successfully for logged-in user")
    void testViewCart_LoggedInUser_Success() {
        // Arrange
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertFalse(viewModel.isEmpty);
        assertEquals(TEST_CART_ID, viewModel.cartId);
        assertEquals(TEST_USER_ID, viewModel.userId);
        assertEquals(1, viewModel.totalItems); // 1 different product
        assertEquals(2, viewModel.totalQuantity); // 2 items total
        assertNotNull(viewModel.formattedTotalAmount);
        assertNotNull(viewModel.items);
        assertEquals(1, viewModel.items.size());
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertEquals(TEST_PRODUCT_ID, item.productId);
        assertEquals(testProduct.getName(), item.productName);
        assertEquals(2, item.quantity);
        assertFalse(item.hasStockWarning);
        
        verify(cartRepository, times(1)).findByUserId(TEST_USER_ID);
        verify(productRepository, times(1)).findById(TEST_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should view cart successfully for guest user")
    void testViewCart_GuestUser_Success() {
        // Arrange
        Cart guestCart = new Cart(null); // Guest cart has no userId
        guestCart.setId(TEST_GUEST_CART_ID);
        CartItem cartItem = new CartItem(
            TEST_PRODUCT_ID,
            testProduct.getName(),
            testProduct.getPrice(),
            1
        );
        guestCart.addItem(cartItem);

        when(cartRepository.findById(TEST_GUEST_CART_ID)).thenReturn(Optional.of(guestCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forGuestUser(TEST_GUEST_CART_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertFalse(viewModel.isEmpty);
        assertEquals(TEST_GUEST_CART_ID, viewModel.cartId);
        assertNull(viewModel.userId); // Guest has no userId
        assertEquals(1, viewModel.totalItems);
        assertEquals(1, viewModel.totalQuantity);
        
        verify(cartRepository, times(1)).findById(TEST_GUEST_CART_ID);
        verify(productRepository, times(1)).findById(TEST_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should display empty cart when user has no cart")
    void testViewCart_NoCart_EmptyView() {
        // Arrange
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.isEmpty);
        assertEquals(0, viewModel.totalItems);
        assertEquals(0, viewModel.totalQuantity);
        assertEquals("0 ₫", viewModel.formattedTotalAmount);
        assertTrue(viewModel.message.contains("trống"));
        
        verify(cartRepository, times(1)).findByUserId(TEST_USER_ID);
        verify(productRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should display empty cart when cart has no items")
    void testViewCart_CartHasNoItems_EmptyView() {
        // Arrange
        Cart emptyCart = new Cart(TEST_USER_ID);
        emptyCart.setId(TEST_CART_ID);

        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(emptyCart));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.isEmpty);
        assertEquals(0, viewModel.totalItems);
        assertEquals(0, viewModel.totalQuantity);
        
        verify(productRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should fail when no userId or guestCartId provided")
    void testViewCart_NoInput_Error() {
        // Arrange - create input data with nulls (using main constructor via reflection workaround)
        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(null);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertNotNull(viewModel.errorMessage);
        
        verify(cartRepository, never()).findByUserId(any());
        verify(cartRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should show stock warning when product is out of stock")
    void testViewCart_ProductOutOfStock_Warning() {
        // Arrange
        testProduct.setStockQuantity(0);

        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.hasStockWarnings);
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertTrue(item.hasStockWarning);
        assertTrue(item.stockWarningMessage.contains("hết hàng"));
        assertEquals("out_of_stock", item.stockStatus);
    }

    @Test
    @DisplayName("Should show stock warning when insufficient stock")
    void testViewCart_InsufficientStock_Warning() {
        // Arrange
        testProduct.setStockQuantity(1); // Only 1 in stock, but cart has 2

        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.hasStockWarnings);
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertTrue(item.hasStockWarning);
        assertTrue(item.stockWarningMessage.contains("Chỉ còn"));
        assertTrue(item.stockWarningMessage.contains("1"));
    }

    @Test
    @DisplayName("Should show warning when product is not available")
    void testViewCart_ProductNotAvailable_Warning() {
        // Arrange
        testProduct.setAvailable(false);

        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.hasStockWarnings);
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertTrue(item.hasStockWarning);
        assertTrue(item.stockWarningMessage.contains("không còn bán"));
    }

    @Test
    @DisplayName("Should show warning when product no longer exists")
    void testViewCart_ProductNotFound_Warning() {
        // Arrange
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.empty());

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertTrue(viewModel.hasStockWarnings);
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertTrue(item.hasStockWarning);
        assertTrue(item.stockWarningMessage.contains("không còn tồn tại"));
        assertEquals(0, item.availableStock);
    }

    @Test
    @DisplayName("Should calculate total amount correctly")
    void testViewCart_TotalAmountCalculation() {
        // Arrange
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        
        // Expected: 38,000,000 * 2 = 76,000,000
        // formattedTotalAmount is a formatted string, not BigDecimal
        assertNotNull(viewModel.formattedTotalAmount);
        assertTrue(viewModel.formattedTotalAmount.contains("76"));
        assertTrue(viewModel.formattedTotalAmount.contains("₫"));
    }

    @Test
    @DisplayName("Should format prices in VND currency")
    void testViewCart_CurrencyFormatting() {
        // Arrange
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.formattedTotalAmount);
        assertTrue(viewModel.formattedTotalAmount.contains("₫") || 
                  viewModel.formattedTotalAmount.contains("đ"));
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertNotNull(item.formattedUnitPrice);
        assertNotNull(item.formattedSubtotal);
    }

    @Test
    @DisplayName("Should display cart summary message")
    void testViewCart_CartSummaryMessage() {
        // Arrange
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.message);
        assertTrue(viewModel.message.contains("Giỏ hàng"));
        assertTrue(viewModel.message.contains("1")); // 1 product
        assertTrue(viewModel.message.contains("2")); // 2 items
    }

    @Test
    @DisplayName("Should show low stock status for products with low inventory")
    void testViewCart_LowStockStatus() {
        // Arrange
        testProduct.setStockQuantity(5); // Low stock (< 10)

        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        viewCartUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        
        ViewCartViewModel.CartItemViewModel item = viewModel.items.get(0);
        assertEquals("low_stock", item.stockStatus);
        assertEquals(5, item.availableStock);
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testViewCart_PresenterCalledOnce() {
        // Arrange
        ViewCartOutputBoundary mockOutputBoundary = mock(ViewCartOutputBoundary.class);
        ViewCartInputBoundary useCase = new ViewCartUseCaseImpl(
            mockOutputBoundary, cartRepository, productRepository
        );
        
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));

        ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(TEST_USER_ID);

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<ViewCartOutputData> captor = ArgumentCaptor.forClass(ViewCartOutputData.class);
        verify(mockOutputBoundary, times(1)).present(captor.capture());
        
        ViewCartOutputData capturedData = captor.getValue();
        assertTrue(capturedData.isSuccess());
        assertFalse(capturedData.isEmpty());
        assertEquals(TEST_CART_ID, capturedData.getCartId());
    }
}
