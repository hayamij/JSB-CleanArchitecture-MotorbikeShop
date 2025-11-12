package com.motorbike.business.usecase.impl;


import com.motorbike.domain.entities.UserRoleRegistry;
import com.motorbike.adapters.presenters.RegisterPresenter;
import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.dto.register.RegisterOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.RegisterInputBoundary;
import com.motorbike.business.usecase.RegisterOutputBoundary;
import com.motorbike.domain.entities.User;
import com.motorbike.domain.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for Register Use Case
 * Tests all business rules and edge cases
 */
@DisplayName("Register Use Case Tests")
class RegisterUseCaseImplTest {

    private UserRepository userRepository;
    private RegisterInputBoundary registerUseCase;
    private RegisterViewModel viewModel;
    private RegisterPresenter presenter;

    // Test data
    private static final String TEST_EMAIL = "newuser@example.com";
    private static final String TEST_USERNAME = "New User";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_PHONE = "0912345678";

    @BeforeEach
    void setUp() {
        // Create mocks
        userRepository = mock(UserRepository.class);
        
        // Create real presenter and view model for integration testing
        viewModel = new RegisterViewModel();
        presenter = new RegisterPresenter(viewModel);
        
        // Create use case with dependencies
        registerUseCase = new RegisterUseCaseImpl(presenter, userRepository);
    }

    @Test
    @DisplayName("Should register successfully with valid data")
    void testRegister_Success() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        
        User savedUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, "HASHED_" + TEST_PASSWORD, TEST_PHONE,
            UserRoleRegistry.customer(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert - Verify repository interactions
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).save(any(User.class));
        
        // Assert - Check view model
        assertTrue(viewModel.success);
        assertFalse(viewModel.hasError);
        assertEquals(savedUser.getId(), viewModel.userId);
        assertEquals(TEST_EMAIL, viewModel.email);
        assertEquals(TEST_USERNAME, viewModel.username);
        assertEquals("Khách hàng", viewModel.roleDisplay);
        assertNotNull(viewModel.registeredAtDisplay);
        assertNotNull(viewModel.message);
        assertTrue(viewModel.message.contains("thành công"));
    }

    @Test
    @DisplayName("Should fail registration when email already exists")
    void testRegister_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, times(1)).existsByEmail(TEST_EMAIL);
        verify(userRepository, never()).save(any());
        
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMAIL_ALREADY_EXISTS", viewModel.errorCode);
        assertNotNull(viewModel.emailError);
        assertTrue(viewModel.errorMessage.contains("đã được đăng ký"));
    }

    @Test
    @DisplayName("Should fail registration when passwords don't match")
    void testRegister_PasswordMismatch() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, "different_password", TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
        
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PASSWORD_MISMATCH", viewModel.errorCode);
        assertNotNull(viewModel.passwordError);
        assertTrue(viewModel.errorMessage.contains("không khớp"));
    }

    @Test
    @DisplayName("Should fail registration with null email")
    void testRegister_NullEmail() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            null, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        verify(userRepository, never()).existsByEmail(any());
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_EMAIL", viewModel.errorCode);
        assertNotNull(viewModel.emailError);
    }

    @Test
    @DisplayName("Should fail registration with empty email")
    void testRegister_EmptyEmail() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            "", TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_EMAIL", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with invalid email format")
    void testRegister_InvalidEmailFormat() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            "invalid-email", TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INVALID_EMAIL_FORMAT", viewModel.errorCode);
        assertNotNull(viewModel.emailError);
    }

    @Test
    @DisplayName("Should fail registration with null username")
    void testRegister_NullUsername() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, null, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_USERNAME", viewModel.errorCode);
        assertNotNull(viewModel.usernameError);
    }

    @Test
    @DisplayName("Should fail registration with empty username")
    void testRegister_EmptyUsername() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, "", TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_USERNAME", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with username too short")
    void testRegister_UsernameTooShort() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, "AB", TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("USERNAME_TOO_SHORT", viewModel.errorCode);
        assertNotNull(viewModel.usernameError);
    }

    @Test
    @DisplayName("Should fail registration with username too long")
    void testRegister_UsernameTooLong() {
        // Arrange
        String longUsername = "A".repeat(51); // 51 characters
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, longUsername, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("USERNAME_TOO_LONG", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with null password")
    void testRegister_NullPassword() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, null, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_PASSWORD", viewModel.errorCode);
        assertNotNull(viewModel.passwordError);
    }

    @Test
    @DisplayName("Should fail registration with empty password")
    void testRegister_EmptyPassword() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, "", "", TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_PASSWORD", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with password too short")
    void testRegister_PasswordTooShort() {
        // Arrange
        String shortPassword = "12345"; // Only 5 characters
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, shortPassword, shortPassword, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("PASSWORD_TOO_SHORT", viewModel.errorCode);
        assertNotNull(viewModel.passwordError);
    }

    @Test
    @DisplayName("Should fail registration with null confirm password")
    void testRegister_NullConfirmPassword() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, null, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_CONFIRM_PASSWORD", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with empty confirm password")
    void testRegister_EmptyConfirmPassword() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, "", TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_CONFIRM_PASSWORD", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with null phone number")
    void testRegister_NullPhone() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, null
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_PHONE", viewModel.errorCode);
        assertNotNull(viewModel.phoneError);
    }

    @Test
    @DisplayName("Should fail registration with empty phone number")
    void testRegister_EmptyPhone() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, ""
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("EMPTY_PHONE", viewModel.errorCode);
    }

    @Test
    @DisplayName("Should fail registration with invalid phone format")
    void testRegister_InvalidPhoneFormat() {
        // Arrange
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, "123" // Too short
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertFalse(viewModel.success);
        assertTrue(viewModel.hasError);
        assertEquals("INVALID_PHONE_FORMAT", viewModel.errorCode);
        assertNotNull(viewModel.phoneError);
    }

    @Test
    @DisplayName("Should hash password before saving")
    void testRegister_PasswordHashing() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        User savedUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, "HASHED_" + TEST_PASSWORD, TEST_PHONE,
            UserRoleRegistry.customer(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        
        // Password should be hashed (not plain text)
        assertNotEquals(TEST_PASSWORD, capturedUser.getPassword());
        assertTrue(capturedUser.getPassword().startsWith("HASHED_"));
    }

    @Test
    @DisplayName("Should set default role as CUSTOMER")
    void testRegister_DefaultRole() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        
        User savedUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, "HASHED_" + TEST_PASSWORD, TEST_PHONE,
            UserRoleRegistry.customer(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertEquals("Khách hàng", viewModel.roleDisplay);
    }

    @Test
    @DisplayName("Should format registration datetime correctly")
    void testRegister_DateTimeFormatting() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        
        User savedUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, "HASHED_" + TEST_PASSWORD, TEST_PHONE,
            UserRoleRegistry.customer(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.registeredAtDisplay);
        // Format: dd/MM/yyyy HH:mm:ss
        assertTrue(viewModel.registeredAtDisplay.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    @DisplayName("Should call presenter exactly once")
    void testRegister_PresenterCalledOnce() {
        // Arrange
        RegisterOutputBoundary mockOutputBoundary = mock(RegisterOutputBoundary.class);
        RegisterInputBoundary useCase = new RegisterUseCaseImpl(mockOutputBoundary, userRepository);
        
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        User savedUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, "HASHED_" + TEST_PASSWORD, TEST_PHONE,
            UserRoleRegistry.customer(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        useCase.execute(inputData);
        
        // Assert
        ArgumentCaptor<RegisterOutputData> captor = ArgumentCaptor.forClass(RegisterOutputData.class);
        verify(mockOutputBoundary, times(1)).present(captor.capture());
        
        RegisterOutputData capturedData = captor.getValue();
        assertTrue(capturedData.isSuccess());
        assertEquals(savedUser.getId(), capturedData.getUserId());
    }

    @Test
    @DisplayName("Should set redirect URL to login page after successful registration")
    void testRegister_RedirectUrl() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        
        User savedUser = new User(
            1L, TEST_EMAIL, TEST_USERNAME, "HASHED_" + TEST_PASSWORD, TEST_PHONE,
            UserRoleRegistry.customer(), true,
            LocalDateTime.now(), LocalDateTime.now(), null
        );
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        RegisterInputData inputData = new RegisterInputData(
            TEST_EMAIL, TEST_USERNAME, TEST_PASSWORD, TEST_PASSWORD, TEST_PHONE
        );
        
        // Act
        registerUseCase.execute(inputData);
        
        // Assert
        assertTrue(viewModel.success);
        assertNotNull(viewModel.redirectUrl);
        assertEquals("/login", viewModel.redirectUrl);
    }
}
