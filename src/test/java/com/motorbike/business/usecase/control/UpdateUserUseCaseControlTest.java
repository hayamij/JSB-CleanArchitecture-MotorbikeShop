package com.motorbike.business.usecase.control;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.updateuser.UpdateUserInputData;
import com.motorbike.business.dto.updateuser.UpdateUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.UpdateUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.ValidationException;

class UpdateUserUseCaseControlTest {

    private UserRepository userRepository;
    private UpdateUserOutputBoundary outputBoundary;
    private UpdateUserUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        outputBoundary = mock(UpdateUserOutputBoundary.class);
        useCase = new UpdateUserUseCaseControl(outputBoundary, userRepository);
    }

    // ===== TC01 =====
    @Test
    void should_return_error_when_input_is_null() {
        useCase.execute(null);

        UpdateUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals(ValidationException.invalidInput().getErrorCode(), output.getErrorCode());
    }

    // ===== TC02 =====
    @Test
    void should_return_error_when_not_admin() {
        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                false,            // admin
                1L,               // userId
                null, null, null, null, null, null, null
        );

        useCase.execute(input);

        UpdateUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals(ValidationException.invalidInput().getErrorCode(), output.getErrorCode());
    }

    // ===== TC03 =====
    @Test
    void should_return_error_when_userId_is_null() {
        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                true,             // admin
                null,             // userId
                null, null, null, null, null, null, null
        );

        useCase.execute(input);

        UpdateUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals(ValidationException.invalidUserId().getErrorCode(), output.getErrorCode());
    }

    // ===== TC04 =====
    @Test
    void should_return_system_error_when_user_not_found() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                true,
                99L,
                "a@gmail.com", null, null, null, null, null, null
        );

        useCase.execute(input);

        UpdateUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
    }

    // ===== TC05 =====
    @Test
    void should_update_user_successfully() {
        TaiKhoan existing = mock(TaiKhoan.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                true,
                1L,
                "new@gmail.com",   // email
                "newname",         // username
                null,              // password
                null,              // phoneNumber
                null,              // address
                null,              // role
                null               // active
        );

        useCase.execute(input);

        UpdateUserOutputData output = captureOutput();
        assertTrue(output.isSuccess());

        verify(existing).setEmail("new@gmail.com");
        verify(existing).setTenDangNhap("newname");
        verify(userRepository).save(existing);
    }

    // ===== TC06 =====
    @Test
    void should_ignore_invalid_role() {
        TaiKhoan existing = mock(TaiKhoan.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                true,
                1L,
                null, null, null, null, null,
                "INVALID_ROLE",
                null
        );

        useCase.execute(input);

        UpdateUserOutputData output = captureOutput();
        assertTrue(output.isSuccess());

        verify(existing, never()).setVaiTro(any(VaiTro.class));
    }

    // ===== TC07 =====
    @Test
    void should_return_system_error_when_repository_throw_exception() {
        when(userRepository.findById(any()))
                .thenThrow(new RuntimeException("DB error"));

        UpdateUserInputData input = UpdateUserInputData.forAdmin(
                true,
                1L,
                "a@gmail.com", null, null, null, null, null, null
        );

        useCase.execute(input);

        UpdateUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
    }

    private UpdateUserOutputData captureOutput() {
        ArgumentCaptor<UpdateUserOutputData> captor = ArgumentCaptor.forClass(UpdateUserOutputData.class);
        verify(outputBoundary).present(captor.capture());
        return captor.getValue();
    }
}
