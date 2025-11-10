package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.User;
import com.motorbike.business.repository.UserRepository;
import com.motorbike.business.usecase.RegisterUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterUseCaseImplTest {

    @Test
    void execute_shouldRegisterUser_whenDataValid() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsByEmail("x@y.com")).thenReturn(false);
        when(userRepo.existsByUsername("ux")).thenReturn(false);

        User saved = new User.Builder()
                .email("x@y.com")
                .username("ux")
                .password("pwd")
                .role("CUSTOMER")
                .active(true)
                .build();
        saved.setId(100L);

        when(userRepo.save(any(User.class))).thenReturn(saved);

        RegisterUseCaseImpl useCase = new RegisterUseCaseImpl(userRepo);
        RegisterUseCase.RegisterRequest req = new RegisterUseCase.RegisterRequest(
                "x@y.com","ux","password123","0123456789"
        );

        RegisterUseCase.RegisterResponse resp = useCase.execute(req);
        assertNotNull(resp);
        assertEquals(100L, resp.getUserId());
    }

    @Test
    void execute_shouldThrowEmailExists_whenEmailTaken() {
        UserRepository userRepo = mock(UserRepository.class);
        when(userRepo.existsByEmail("taken@x.com")).thenReturn(true);

        RegisterUseCaseImpl useCase = new RegisterUseCaseImpl(userRepo);
        RegisterUseCase.RegisterRequest req = new RegisterUseCase.RegisterRequest(
                "taken@x.com","u","password123","0123"
        );

        assertThrows(EmailAlreadyExistsException.class, () -> useCase.execute(req));
    }
}
