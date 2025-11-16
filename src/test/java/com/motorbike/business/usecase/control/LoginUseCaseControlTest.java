package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.dto.login.LoginOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.VaiTro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for LoginUseCaseControl
 * Tests all business rules for login functionality
 */
@DisplayName("Login Use Case Tests")
class LoginUseCaseControlTest {

    private LoginUseCaseControl loginUseCase;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private LoginOutputBoundary outputBoundary;
    private TaiKhoan validUser;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(LoginOutputBoundary.class);
        
        loginUseCase = new LoginUseCaseControl(outputBoundary, userRepository, cartRepository);
        
        // Create valid test user
        validUser = new TaiKhoan(
                1L,
                "test@example.com",
                "testuser",
                "validPassword123",
                "0123456789",
                "123 Test Street",
                VaiTro.CUSTOMER,
                true,
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now(),
                null
        );
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLoginSuccess() {
        // Arrange
        LoginInputData inputData = new LoginInputData("test@example.com", "validPassword123", null);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(validUser));
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).save(validUser); // Should update last login
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(true, output.isSuccess());
        assertEquals(1L, output.getUserId());
        assertEquals("test@example.com", output.getEmail());
    }

    @Test
    @DisplayName("Should merge guest cart when login with guest cart ID")
    void testLoginWithGuestCartMerge() {
        // Arrange
        Long guestCartId = 999L;
        LoginInputData inputData = new LoginInputData("test@example.com", "validPassword123", guestCartId);
        GioHang guestCart = new GioHang(guestCartId);
        GioHang userCart = new GioHang(1L);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(validUser));
        when(cartRepository.findById(guestCartId)).thenReturn(Optional.of(guestCart));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(userCart));
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(cartRepository).findById(guestCartId);
        verify(cartRepository).findByUserId(1L);
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        assertEquals(true, captor.getValue().isSuccess());
    }

    @Test
    @DisplayName("Should fail when user not found")
    void testLoginFailUserNotFound() {
        // Arrange
        LoginInputData inputData = new LoginInputData("notfound@example.com", "password", null);
        
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).save(any());
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(false, output.isSuccess());
        assertEquals("USER_NOT_FOUND", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when password is incorrect")
    void testLoginFailWrongPassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData("test@example.com", "wrongPassword", null);
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(validUser));
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).save(any());
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(false, output.isSuccess());
        assertEquals("WRONG_PASSWORD", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when account is locked")
    void testLoginFailAccountLocked() {
        // Arrange
        TaiKhoan lockedUser = new TaiKhoan(
                1L,
                "locked@example.com",
                "lockeduser",
                "password123",
                "0123456789",
                "123 Test Street",
                VaiTro.CUSTOMER,
                false, // Account is locked
                LocalDateTime.now().minusDays(7),
                LocalDateTime.now(),
                null
        );
        
        LoginInputData inputData = new LoginInputData("locked@example.com", "password123", null);
        
        when(userRepository.findByEmail("locked@example.com")).thenReturn(Optional.of(lockedUser));
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).save(any());
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(false, output.isSuccess());
        assertEquals("ACCOUNT_LOCKED", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testLoginFailNullInput() {
        // Act
        loginUseCase.execute(null);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(false, output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with empty email")
    void testLoginFailEmptyEmail() {
        // Arrange
        LoginInputData inputData = new LoginInputData("", "password", null);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(false, output.isSuccess());
        assertEquals("EMPTY_EMAIL", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with empty password")
    void testLoginFailEmptyPassword() {
        // Arrange
        LoginInputData inputData = new LoginInputData("test@example.com", "", null);
        
        // Act
        loginUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).findByEmail(any());
        
        ArgumentCaptor<LoginOutputData> captor = ArgumentCaptor.forClass(LoginOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        LoginOutputData output = captor.getValue();
        assertEquals(false, output.isSuccess());
        assertEquals("EMPTY_PASSWORD", output.getErrorCode());
    }
}
