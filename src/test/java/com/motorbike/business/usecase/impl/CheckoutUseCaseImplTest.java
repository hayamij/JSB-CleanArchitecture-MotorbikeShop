package com.motorbike.business.usecase.impl;



import com.motorbike.domain.entities.ProductCategoryRegistry;
import com.motorbike.domain.entities.UserRoleRegistry;
import com.motorbike.adapters.presenters.CheckoutPresenter;
import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.CheckoutInputBoundary;
import com.motorbike.business.usecase.CheckoutOutputBoundary;
import com.motorbike.domain.entities.*;
import com.motorbike.domain.repositories.OrderRepository;
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

@DisplayName("Checkout Use Case Tests")
class CheckoutUseCaseImplTest {
    
    private CheckoutInputBoundary checkoutUseCase;
    private CheckoutPresenter presenter;
    private CheckoutViewModel viewModel;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_CART_ID = 100L;
    private static final Long TEST_PRODUCT_ID = 1L;
    private static final Long TEST_ORDER_ID = 1000L;
    private static final String TEST_SHIPPING_ADDRESS = "123 Nguyễn Huệ, Quận 1, TP.HCM";
    private static final String TEST_PHONE = "0909123456";

    private User testUser;
    private Cart testCart;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        
        presenter = new CheckoutPresenter();
        checkoutUseCase = new CheckoutUseCaseImpl(
            presenter, userRepository, cartRepository, productRepository, orderRepository
        );

        // Setup test user
        testUser = new User(
            TEST_USER_ID,
            "customer@test.com",
            "Test Customer",
            "hashedpassword",
            "0909999999",
            UserRoleRegistry.customer(),
            true,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );

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
    @DisplayName("Should checkout successfully with valid data")
    void testCheckout_Success() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(
            TEST_ORDER_ID,
            TEST_USER_ID,
            testUser.getUsername(),
            testUser.getEmail(),
            TEST_PHONE,
            TEST_SHIPPING_ADDRESS,
            OrderStatus.PENDING,
            new BigDecimal("76000000"),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertTrue(viewModel.success);
        assertFalse(viewModel.hasError);
        assertEquals(TEST_ORDER_ID, viewModel.orderId);
        assertEquals(testUser.getUsername(), viewModel.customerName);
        assertEquals(testUser.getEmail(), viewModel.customerEmail);
        assertEquals(TEST_PHONE, viewModel.customerPhone);
        assertEquals(TEST_SHIPPING_ADDRESS, viewModel.shippingAddress);
        assertEquals("Chờ xử lý", viewModel.orderStatus);
        assertNotNull(viewModel.formattedTotalAmount);
        assertNotNull(viewModel.items);
        assertEquals(1, viewModel.items.size());
        
        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verify(cartRepository, times(1)).findByUserId(TEST_USER_ID);
        verify(productRepository, times(2)).findById(TEST_PRODUCT_ID); // Called twice: stock check + create order item
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(any(Product.class)); // Stock deduction
        verify(cartRepository, times(1)).delete(TEST_CART_ID); // Cart cleared
    }

    @Test
    @DisplayName("Should fail when user is not logged in")
    void testCheckout_UserNotLoggedIn() {
        // Arrange
        CheckoutInputData inputData = new CheckoutInputData(
            null, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("USER_NOT_LOGGED_IN", viewModel.errorCode);
        assertTrue(viewModel.message.contains("đăng nhập"));
        
        verify(userRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when user not found")
    void testCheckout_UserNotFound() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("USER_NOT_FOUND", viewModel.errorCode);
        
        verify(userRepository, times(1)).findById(TEST_USER_ID);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when cart is empty")
    void testCheckout_CartEmpty() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("CART_EMPTY", viewModel.errorCode);
        assertTrue(viewModel.message.contains("Giỏ hàng trống"));
        
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when cart has no items")
    void testCheckout_CartHasNoItems() {
        // Arrange
        Cart emptyCart = new Cart(TEST_USER_ID);
        emptyCart.setId(TEST_CART_ID);
        
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(emptyCart));
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("CART_EMPTY", viewModel.errorCode);
        
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when product not found")
    void testCheckout_ProductNotFound() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.empty());
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PRODUCT_NOT_FOUND", viewModel.errorCode);
        assertTrue(viewModel.message.contains("không còn tồn tại"));
        
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when product is not available")
    void testCheckout_ProductNotAvailable() {
        // Arrange
        testProduct.setAvailable(false);
        
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PRODUCT_NOT_AVAILABLE", viewModel.errorCode);
        assertTrue(viewModel.message.contains("không còn bán"));
        
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when insufficient stock")
    void testCheckout_InsufficientStock() {
        // Arrange
        testProduct.setStockQuantity(1); // Only 1 in stock, but cart has 2
        
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INSUFFICIENT_STOCK", viewModel.errorCode);
        assertTrue(viewModel.message.contains("chỉ còn"));
        assertTrue(viewModel.message.contains("1"));
        
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should fail when shipping address is not provided")
    void testCheckout_NoShippingAddress() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, null, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("SHIPPING_ADDRESS_REQUIRED", viewModel.errorCode);
        assertTrue(viewModel.message.contains("địa chỉ giao hàng"));
        
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should deduct stock quantity after checkout")
    void testCheckout_StockDeduction() {
        // Arrange
        int initialStock = testProduct.getStockQuantity();
        int orderQuantity = testCart.getItems().get(0).getQuantity();
        
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, "Test", "test@test.com",
                                     TEST_PHONE, TEST_SHIPPING_ADDRESS, OrderStatus.PENDING,
                                     new BigDecimal("76000000"), LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        
        Product savedProduct = productCaptor.getValue();
        assertEquals(initialStock - orderQuantity, savedProduct.getStockQuantity());
    }

    @Test
    @DisplayName("Should clear cart after successful checkout")
    void testCheckout_ClearCart() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, "Test", "test@test.com",
                                     TEST_PHONE, TEST_SHIPPING_ADDRESS, OrderStatus.PENDING,
                                     new BigDecimal("76000000"), LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);

        // Assert
        verify(cartRepository, times(1)).delete(TEST_CART_ID);
    }

    @Test
    @DisplayName("Should create order with correct customer information")
    void testCheckout_OrderCustomerInfo() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, testUser.getUsername(),
                                     testUser.getEmail(), TEST_PHONE, TEST_SHIPPING_ADDRESS,
                                     OrderStatus.PENDING, new BigDecimal("76000000"),
                                     LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(TEST_USER_ID, capturedOrder.getCustomerId());
        assertEquals(testUser.getUsername(), capturedOrder.getCustomerName());
        assertEquals(testUser.getEmail(), capturedOrder.getCustomerEmail());
        assertEquals(TEST_PHONE, capturedOrder.getCustomerPhone());
        assertEquals(TEST_SHIPPING_ADDRESS, capturedOrder.getShippingAddress());
        assertEquals(OrderStatus.PENDING, capturedOrder.getStatus());
    }

    @Test
    @DisplayName("Should use user's phone number if not provided")
    void testCheckout_DefaultPhoneNumber() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, testUser.getUsername(),
                                     testUser.getEmail(), testUser.getPhoneNumber(),
                                     TEST_SHIPPING_ADDRESS, OrderStatus.PENDING,
                                     new BigDecimal("76000000"), LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, null // No phone provided
        );

        // Act
        checkoutUseCase.execute(inputData);

        // Assert
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(testUser.getPhoneNumber(), capturedOrder.getCustomerPhone());
    }

    @Test
    @DisplayName("Should format order status correctly in Vietnamese")
    void testCheckout_OrderStatusFormatting() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, "Test", "test@test.com",
                                     TEST_PHONE, TEST_SHIPPING_ADDRESS, OrderStatus.PENDING,
                                     new BigDecimal("76000000"), LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertEquals("Chờ xử lý", viewModel.orderStatus);
    }

    @Test
    @DisplayName("Should format total amount in VND currency")
    void testCheckout_CurrencyFormatting() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, "Test", "test@test.com",
                                     TEST_PHONE, TEST_SHIPPING_ADDRESS, OrderStatus.PENDING,
                                     new BigDecimal("76000000"), LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        checkoutUseCase.execute(inputData);
        viewModel = presenter.getViewModel();

        // Assert
        assertNotNull(viewModel.formattedTotalAmount);
        assertTrue(viewModel.formattedTotalAmount.contains("₫") || 
                  viewModel.formattedTotalAmount.contains("đ"));
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testCheckout_PresenterCalledOnce() {
        // Arrange
        CheckoutOutputBoundary mockOutputBoundary = mock(CheckoutOutputBoundary.class);
        CheckoutInputBoundary useCase = new CheckoutUseCaseImpl(
            mockOutputBoundary, userRepository, cartRepository, productRepository, orderRepository
        );
        
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(TEST_PRODUCT_ID)).thenReturn(Optional.of(testProduct));
        
        Order savedOrder = new Order(TEST_ORDER_ID, TEST_USER_ID, "Test", "test@test.com",
                                     TEST_PHONE, TEST_SHIPPING_ADDRESS, OrderStatus.PENDING,
                                     new BigDecimal("76000000"), LocalDateTime.now(), LocalDateTime.now());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        CheckoutInputData inputData = new CheckoutInputData(
            TEST_USER_ID, TEST_SHIPPING_ADDRESS, TEST_PHONE
        );

        // Act
        useCase.execute(inputData);

        // Assert
        ArgumentCaptor<CheckoutOutputData> captor = ArgumentCaptor.forClass(CheckoutOutputData.class);
        verify(mockOutputBoundary, times(1)).present(captor.capture());
        
        CheckoutOutputData capturedData = captor.getValue();
        assertTrue(capturedData.isSuccess());
        assertEquals(TEST_ORDER_ID, capturedData.getOrderId());
    }
}
