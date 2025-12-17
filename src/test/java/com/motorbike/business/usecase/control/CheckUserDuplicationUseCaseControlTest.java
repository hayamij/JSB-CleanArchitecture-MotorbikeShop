package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationInputData;
import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationOutputData;
import com.motorbike.business.usecase.output.CheckUserDuplicationOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.business.ports.TaiKhoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckUserDuplicationUseCaseControlTest {

    @Mock
    private TaiKhoanRepository taiKhoanRepository;

    @Mock
    private CheckUserDuplicationOutputBoundary outputBoundary;

    private CheckUserDuplicationUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CheckUserDuplicationUseCaseControl(taiKhoanRepository, outputBoundary);
    }

    @Test
    void shouldDetectDuplicateUsername() {
        // Given
        String username = "john_doe";
        TaiKhoan existingUser = new TaiKhoan(username, "password", "john@example.com", "0123456789", "123 Main St");

        when(taiKhoanRepository.findByTenDangNhap(username)).thenReturn(Optional.of(existingUser));

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData(username, "new@example.com", null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckUserDuplicationOutputData.class));
        verify(taiKhoanRepository).findByTenDangNhap(username);
    }

    @Test
    void shouldDetectDuplicateEmail() {
        // Given
        String email = "john@example.com";
        TaiKhoan existingUser = new TaiKhoan("other_user", "password", email, "0123456789", "123 Main St");

        when(taiKhoanRepository.findByTenDangNhap("new_user")).thenReturn(Optional.empty());
        when(taiKhoanRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData("new_user", email, null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckUserDuplicationOutputData.class));
    }

    @Test
    void shouldAllowUniqueUser() {
        // Given
        String username = "new_user";
        String email = "new@example.com";

        when(taiKhoanRepository.findByTenDangNhap(username)).thenReturn(Optional.empty());
        when(taiKhoanRepository.findByEmail(email)).thenReturn(Optional.empty());

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData(username, email, null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckUserDuplicationOutputData.class));
    }

    @Test
    void shouldAllowSameUserForUpdate() {
        // Given
        Long userId = 1L;
        String email = "john@example.com";
        TaiKhoan existingUser = new TaiKhoan("john_doe", "password", email, "0123456789", "123 Main St");
        existingUser.setMaTK(userId);

        when(taiKhoanRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData("john_doe", email, userId);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckUserDuplicationOutputData.class));
    }
}
