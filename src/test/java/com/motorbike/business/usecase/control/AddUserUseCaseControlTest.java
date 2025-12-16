package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.adduser.AddUserInputData;
import com.motorbike.business.dto.adduser.AddUserOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.AddUserOutputBoundary;

class AddUserUseCaseControlTest {

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private AddUserOutputBoundary outputBoundary;
    private AddUserUseCaseControl useCase;


    // ===== TC01: input null => error =====
    @Test
    void should_return_error_when_input_is_null() {
        useCase.execute(null);

        AddUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertNotNull(output.getErrorCode());
    }

    // ===== TC02: email đã tồn tại => error =====
    @Test
    void should_return_error_when_email_exists() {
        AddUserInputData input = AddUserInputData.of(
                "a@gmail.com", "user", "pw", "0909", "HCM", "USER", true
        );

        when(userRepository.existsByEmail("a@gmail.com")).thenReturn(true);

        useCase.execute(input);

        AddUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertNotNull(output.getErrorCode());
    }

    // ===== TC03: hợp lệ => success + tạo cart =====
    @Test
    void should_create_user_and_cart_successfully() {
        AddUserInputData input = AddUserInputData.of(
                "a@gmail.com", "user", "pw", "0909", "HCM", "USER", true
        );

        when(userRepository.existsByEmail("a@gmail.com")).thenReturn(false);

        // save user ok
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        // save cart ok
        when(cartRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(input);

        AddUserOutputData output = captureOutput();
        assertTrue(output.isSuccess());

        // ✅ đảm bảo có gọi tạo giỏ hàng
        verify(cartRepository, times(1)).save(any());
    }

    // ===== TC04: repo throw exception => SYSTEM_ERROR =====
    @Test
    void should_return_system_error_when_repository_throw_exception() {
        AddUserInputData input = AddUserInputData.of(
                "a@gmail.com", "user", "pw", "0909", "HCM", "USER", true
        );

        when(userRepository.existsByEmail(any())).thenThrow(new RuntimeException("DB error"));

        useCase.execute(input);

        AddUserOutputData output = captureOutput();
        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
    }

    private AddUserOutputData captureOutput() {
        ArgumentCaptor<AddUserOutputData> captor = ArgumentCaptor.forClass(AddUserOutputData.class);
        verify(outputBoundary).present(captor.capture());
        return captor.getValue();
    }
}
