package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.User;
import com.motorbike.business.repository.UserRepository;
import com.motorbike.business.usecase.LoginUseCase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginUseCaseImplTest {

    @Test
    void execute_shouldReturnLoginResponse_whenCredentialsValid() {
        UserRepository userRepo = mock(UserRepository.class);
        User user = new User();
        user.setId(10L);
        user.setEmail("test@example.com");
        user.setPassword("secret");
        user.setUsername("tester");
        user.setRole("CUSTOMER");
        user.setActive(true);

        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        LoginUseCaseImpl useCase = new LoginUseCaseImpl(userRepo);
        LoginUseCase.LoginRequest req = new LoginUseCase.LoginRequest("test@example.com", "secret");

        LoginUseCase.LoginResponse resp = useCase.execute(req);

        assertTrue(resp.isSuccess());
        assertEquals("test@example.com", resp.getEmail());
        assertNotNull(resp.getToken());
    }

    @Test
    void execute_shouldThrowInvalidCredentials_whenPasswordWrong() {
        UserRepository userRepo = mock(UserRepository.class);
        User user = new User();
        user.setId(11L);
        user.setEmail("a@b.com");
        user.setPassword("correct");
        user.setUsername("u");
        user.setRole("CUSTOMER");
        user.setActive(true);

        when(userRepo.findByEmail("a@b.com")).thenReturn(Optional.of(user));

        LoginUseCaseImpl useCase = new LoginUseCaseImpl(userRepo);
        LoginUseCase.LoginRequest req = new LoginUseCase.LoginRequest("a@b.com", "wrong");

        assertThrows(InvalidCredentialsException.class, () -> useCase.execute(req));
    }
}
