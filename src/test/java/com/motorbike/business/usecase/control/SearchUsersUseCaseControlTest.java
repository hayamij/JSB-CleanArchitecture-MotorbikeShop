package com.motorbike.business.usecase.control;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.listusers.ListUsersInputData;
import com.motorbike.business.dto.listusers.ListUsersOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.ListUsersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;

class SearchUsersUseCaseControlTest {

    private UserRepository userRepository;
    private ListUsersOutputBoundary outputBoundary;
    private ListUsersUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        outputBoundary = mock(ListUsersOutputBoundary.class);
        useCase = new ListUsersUseCaseControl(outputBoundary, userRepository);
    }

    @Test
    void search_should_match_email() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "admin@gmail.com");
        useCase.execute(input);

        ListUsersOutputData output = captureOutput();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getUsers().size());
    }

    @Test
    void search_should_match_username() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "admin");
        useCase.execute(input);

        ListUsersOutputData output = captureOutput();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getUsers().size());
    }

    @Test
    void search_should_be_case_insensitive() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "AdMiN");
        useCase.execute(input);

        ListUsersOutputData output = captureOutput();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getUsers().size());
    }

    @Test
    void search_should_trim_keyword() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "   admin   ");
        useCase.execute(input);

        ListUsersOutputData output = captureOutput();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getUsers().size());
    }

    @Test
    void search_should_return_empty_list_when_no_match() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "xyz");
        useCase.execute(input);

        ListUsersOutputData output = captureOutput();
        assertTrue(output.isSuccess());
        assertEquals(0, output.getUsers().size());
    }

    private ListUsersOutputData captureOutput() {
        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());
        return captor.getValue();
    }

    private List<TaiKhoan> mockUsers() {
        LocalDateTime now = LocalDateTime.now();

        TaiKhoan admin = new TaiKhoan(
                1L, "admin@gmail.com", "admin",
                "pw", "0900000000", "HCM",
                null, true, now, now, now
        );

        TaiKhoan user = new TaiKhoan(
                2L, "user@gmail.com", "user",
                "pw", "0911111111", "HN",
                null, true, now, now, now
        );

        return List.of(admin, user);
    }
}
