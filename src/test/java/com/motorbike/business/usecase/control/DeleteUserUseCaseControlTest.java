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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.deleteuser.DeleteUserInputData;
import com.motorbike.business.dto.deleteuser.DeleteUserOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.exceptions.ValidationException;

class DeleteUserUseCaseControlTest {

    private UserRepository userRepository;
    private DeleteUserOutputBoundary outputBoundary;
    private DeleteUserUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        outputBoundary = mock(DeleteUserOutputBoundary.class);
        useCase = new DeleteUserUseCaseControl(outputBoundary, userRepository);
    }

    // ===== TC01 =====
    @Test
    void should_return_error_when_input_is_null() {
        useCase.execute(null);

        DeleteUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals(
                ValidationException.invalidInput().getErrorCode(),
                output.getErrorCode()
        );
    }

    // ===== TC02 =====
    @Test
    void should_return_error_when_not_admin() {
        DeleteUserInputData input = DeleteUserInputData.forAdmin(false, 1L);

        useCase.execute(input);

        DeleteUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals(
                ValidationException.invalidInput().getErrorCode(),
                output.getErrorCode()
        );
    }

    // ===== TC03 =====
    @Test
    void should_return_error_when_userId_is_null() {
        DeleteUserInputData input = DeleteUserInputData.forAdmin(true, null);

        useCase.execute(input);

        DeleteUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals(
                ValidationException.invalidUserId().getErrorCode(),
                output.getErrorCode()
        );
    }

    // ===== TC04 =====
    @Test
    void should_return_error_when_user_not_found() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        DeleteUserInputData input = DeleteUserInputData.forAdmin(true, 99L);
        useCase.execute(input);

        DeleteUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
    }

    // ===== TC05 =====
    @Test
    void should_delete_user_successfully() {
        TaiKhoan user = mock(TaiKhoan.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        DeleteUserInputData input = DeleteUserInputData.forAdmin(true, 1L);
        useCase.execute(input);

        DeleteUserOutputData output = captureOutput();
        assertTrue(output.isSuccess());

        verify(userRepository, times(1)).deleteById(1L);
    }

    // ===== TC06 =====
    @Test
    void should_return_system_error_when_repository_throw_exception() {
        when(userRepository.findById(any()))
                .thenThrow(new RuntimeException("DB error"));

        DeleteUserInputData input = DeleteUserInputData.forAdmin(true, 1L);
        useCase.execute(input);

        DeleteUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
    }

    // ===== helper =====
    private DeleteUserOutputData captureOutput() {
        ArgumentCaptor<DeleteUserOutputData> captor =
                ArgumentCaptor.forClass(DeleteUserOutputData.class);
        verify(outputBoundary).present(captor.capture());
        return captor.getValue();
    }
}
