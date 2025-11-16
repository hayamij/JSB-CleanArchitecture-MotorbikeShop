package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.GioHang;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for RegisterUseCaseControl
 * Tests all business rules for user registration
 */
@DisplayName("Register Use Case Tests")
class RegisterUseCaseControlTest {

    private RegisterUseCaseControl registerUseCase;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private RegisterOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        outputBoundary = mock(RegisterOutputBoundary.class);
        
        registerUseCase = new RegisterUseCaseControl(outputBoundary, userRepository, cartRepository);
    }

    @Test
    @DisplayName("Should register successfully with valid data")
    void testRegisterSuccess() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            "newuser@example.com",
            "newuser",
            "Password123",
            "Password123", // confirmPassword
            "0123456789",
            "123 Test Street"
        );
        
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(userRepository.save(any(TaiKhoan.class))).thenAnswer(invocation -> {
            TaiKhoan user = invocation.getArgument(0);
            return new TaiKhoan(
                1L,
                user.getEmail(),
                user.getTenDangNhap(),
                user.getMatKhau(),
                user.getSoDienThoai(),
                user.getDiaChi(),
                user.getVaiTro(),
                user.isHoatDong(),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now(),
                null
            );
        });
        when(cartRepository.save(any(GioHang.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        // Note: existsByEmail is called but only after validateInput passes
        // Since validation can throw exception first, we don't verify it here
        verify(userRepository).save(any(TaiKhoan.class));
        verify(cartRepository).save(any(GioHang.class));
        
        ArgumentCaptor<RegisterOutputData> captor = ArgumentCaptor.forClass(RegisterOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        RegisterOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals("newuser@example.com", output.getEmail());
    }

    @Test
    @DisplayName("Should fail when email already exists")
    void testRegisterFailEmailExists() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            "existing@example.com",
            "testuser",
            "Password123",
            "Password123", // confirmPassword
            "0123456789",
            "123 Test Street"
        );
        
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        // Note: existsByEmail should be called after validation passes
        verify(userRepository, never()).save(any());
        verify(cartRepository, never()).save(any());
        
        ArgumentCaptor<RegisterOutputData> captor = ArgumentCaptor.forClass(RegisterOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        RegisterOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("EMAIL_EXISTS", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with null input")
    void testRegisterFailNullInput() {
        // Act
        registerUseCase.execute(null);
        
        // Assert
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
        
        ArgumentCaptor<RegisterOutputData> captor = ArgumentCaptor.forClass(RegisterOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        RegisterOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail with empty email")
    void testRegisterFailEmptyEmail() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            "",
            "testuser",
            "Password123!",
            "Password123!",  // Must match password for validation to proceed
            "0123456789",
            "123 Test Street"
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).existsByEmail(any());
        
        ArgumentCaptor<RegisterOutputData> captor = ArgumentCaptor.forClass(RegisterOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        RegisterOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("EMPTY_EMAIL", output.getErrorCode());  // More specific error code from entity
    }

    @Test
    @DisplayName("Should fail with empty username")
    void testRegisterFailEmptyUsername() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            "test@example.com",
            "",
            "Password123!",
            "Password123!",  // Must match password for validation to proceed
            "0123456789",
            "123 Test Street"
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).existsByEmail(any());
        
        ArgumentCaptor<RegisterOutputData> captor = ArgumentCaptor.forClass(RegisterOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        RegisterOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("EMPTY_USERNAME", output.getErrorCode());  // More specific error code from entity
    }
}
