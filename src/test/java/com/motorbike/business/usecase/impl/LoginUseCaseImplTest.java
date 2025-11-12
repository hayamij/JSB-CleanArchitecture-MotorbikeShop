package com.motorbike.business.usecase.impl;

import com.motorbike.adapters.presenters.LoginPresenter;
import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.LoginInputBoundary;
import com.motorbike.business.usecase.LoginOutputBoundary;
import com.motorbike.domain.entities.Cart;
import com.motorbike.domain.entities.User;
import com.motorbike.domain.entities.UserRoleRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for Login Use Case
 * Tests all business rules and edge cases
 */
@DisplayName("Login Use Case Tests")
class LoginUseCaseImplTest {

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private LoginInputBoundary loginUseCase;
    private LoginViewModel viewModel;
    private LoginPresenter presenter;

    // Test data
    private User testUser;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "Test User";

    @BeforeEach
    void setUp() {
        // Create mocks
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        
        // Create real presenter and view model for integration testing
        viewModel = new LoginViewModel();
        presenter = new LoginPresenter(viewModel);
        
        // Create use case with dependencies
        loginUseCase = new LoginUseCaseImpl(presenter, userRepository, cartRepository);
        
        // Setup test user
        testUser = new User(
            1L,
            TEST_EMAIL,
            TEST_USERNAME,
            TEST_PASSWORD, // In reality would be hashed
            "0123456789",
            UserRoleRegistry.customer(),
            true,
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().minusDays(1),
            null
        );
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLogin_Success() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert - Verify repository interactions
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).updateLastLogin(testUser.getId());
        
        // Assert - Check view model
        assertTrue(viewModel.success);
        assertFalse(viewModel.hasError);
        assertEquals(testUser.getId(), viewModel.userId);
        assertEquals(TEST_EMAIL, viewModel.email);
        assertEquals(TEST_USERNAME, viewModel.username);
        assertEquals("Khách hàng", viewModel.roleDisplay);
        assertNotNull(viewModel.sessionToken);
        assertNotNull(viewModel.message);
        assertTrue(viewModel.message.contains("Đăng nhập thành công"));
    }

    @Test
    @DisplayName("Should fail login when email not found")
    void testLogin_EmailNotFound() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(userRepository, never()).updateLastLogin(any());
        
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMAIL_NOT_FOUND", viewModel.errorCode);
        assertNotNull(viewModel.errorMessage);
        assertTrue(viewModel.errorMessage.contains("Email không tồn tại"));
    }

    @Test
    @DisplayName("Should fail login when password is incorrect")
    void testLogin_InvalidPassword() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, "wrong_password");
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(userRepository, never()).updateLastLogin(any());
        
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INVALID_PASSWORD", viewModel.errorCode);
        assertNotNull(viewModel.errorMessage);
        assertTrue(viewModel.errorMessage.contains("Mật khẩu không chính xác"));
    }

    @Test
    @DisplayName("Should fail login when user is inactive")
    void testLogin_UserInactive() {
        // Arrange
        User inactiveUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, "0123456789",
            UserRoleRegistry.customer(), false, // inactive
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(inactiveUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("USER_INACTIVE", viewModel.errorCode);
        assertTrue(viewModel.errorMessage.contains("vô hiệu hóa"));
    }

    @Test
    @DisplayName("Should fail login with null email")
    void testLogin_NullEmail() {
        // Arrange
        LoginInputData inputData = new LoginInputData(null, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_EMAIL", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail login with empty email")
    void testLogin_EmptyEmail() {
        // Arrange
        LoginInputData inputData = new LoginInputData("", TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_EMAIL", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail login with invalid email format")
    void testLogin_InvalidEmailFormat() {
        // Arrange
        LoginInputData inputData = new LoginInputData("invalid-email", TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INVALID_EMAIL_FORMAT", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail login with null password")
    void testLogin_NullPassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, null);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_PASSWORD", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail login with empty password")
    void testLogin_EmptyPassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, "");
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_PASSWORD", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail login with short password")
    void testLogin_PasswordTooShort() {
        // Arrange
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, "12345"); // Only 5 chars
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PASSWORD_TOO_SHORT", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should display admin role correctly")
    void testLogin_AdminRole() {
        // Arrange
        User adminUser = new User(
            1L, "admin@example.com", "Admin User", TEST_PASSWORD, "0123456789",
            UserRoleRegistry.admin(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));
        LoginInputData inputData = new LoginInputData("admin@example.com", TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertEquals("Quản trị viên", viewModel.roleDisplay);
    }

    @Test
    @DisplayName("Should merge guest cart when user has no cart")
    void testLogin_MergeGuestCartToNewUserCart() {
        // Arrange
        Long guestCartId = 100L;
        Cart guestCart = new Cart(null); // Guest cart with no user
        guestCart.setId(guestCartId);
        
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(testUser.getId())).thenReturn(Optional.empty());
        when(cartRepository.findById(guestCartId)).thenReturn(Optional.of(guestCart));
        
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD, guestCartId);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        verify(cartRepository).save(guestCart);
        assertTrue(viewModel.cartMerged);
        assertNotNull(viewModel.cartMergeMessage);
    }

    @Test
    @DisplayName("Should merge guest cart when user has existing cart")
    void testLogin_MergeGuestCartToExistingUserCart() {
        // Arrange
        Long guestCartId = 100L;
        Long userCartId = 200L;
        Cart userCart = new Cart(testUser.getId());
        userCart.setId(userCartId);
        
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(cartRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(userCart));
        when(cartRepository.mergeGuestCartToUserCart(guestCartId, userCartId)).thenReturn(3);
        
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD, guestCartId);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        verify(cartRepository).mergeGuestCartToUserCart(guestCartId, userCartId);
        assertTrue(viewModel.cartMerged);
        assertNotNull(viewModel.cartMergeMessage);
        assertTrue(viewModel.cartMergeMessage.contains("3 sản phẩm"));
    }

    @Test
    @DisplayName("Should not merge cart when no guest cart provided")
    void testLogin_NoGuestCart() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD); // No guest cart
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        verify(cartRepository, never()).findByUserId(any());
        verify(cartRepository, never()).mergeGuestCartToUserCart(any(), any());
        assertFalse(viewModel.cartMerged);
        assertNull(viewModel.cartMergeMessage);
    }

    @Test
    @DisplayName("Should generate session token on successful login")
    void testLogin_SessionTokenGenerated() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.sessionToken);
        assertTrue(viewModel.sessionToken.startsWith("SESSION_"));
        assertTrue(viewModel.sessionToken.contains(String.valueOf(testUser.getId())));
    }

    @Test
    @DisplayName("Should update last login timestamp")
    void testLogin_UpdateLastLogin() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, times(1)).updateLastLogin(testUser.getId());
    }

    @Test
    @DisplayName("Should format last login time correctly")
    void testLogin_LastLoginFormatting() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.lastLoginDisplay);
        // Format: dd/MM/yyyy HH:mm:ss
        assertTrue(viewModel.lastLoginDisplay.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testLogin_PresenterCalledOnce() {
        // Arrange
        LoginOutputBoundary mockOutputBoundary = mock(LoginOutputBoundary.class);
        LoginInputBoundary useCase = new LoginUseCaseImpl(mockOutputBoundary, userRepository, cartRepository);
        
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        LoginInputData inputData = new LoginInputData(TEST_EMAIL, TEST_PASSWORD);
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(mockOutputBoundary, times(1)).present(captor.capture());
        
        LoginOutputData capturedData = captor.getValue();
        assertTrue(capturedData.isSuccess());
        assertEquals(testUser.getId(), capturedData.getUserId());
    }
}
