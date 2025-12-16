package com.motorbike.business.usecase.control;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

class ListUsersUseCaseControlTest {

    private UserRepository userRepository;
    private ListUsersOutputBoundary outputBoundary;
    private ListUsersUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        outputBoundary = mock(ListUsersOutputBoundary.class);
        useCase = new ListUsersUseCaseControl(outputBoundary, userRepository);
    }

    // ===== TC01 =====
    @Test
    void should_return_error_when_not_admin() {
        ListUsersInputData input = ListUsersInputData.forAdmin(false, null);

        useCase.execute(input);

        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());

        ListUsersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertNotNull(output.getErrorCode());
    }

    // ===== TC02 =====
    @Test
    void should_return_error_when_input_is_null() {
        useCase.execute(null);

        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());

        ListUsersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertNotNull(output.getErrorCode());
    }

    // ===== TC03 =====
    @Test
    void should_return_all_users_when_admin_and_no_keyword() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, null);
        useCase.execute(input);

        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());

        ListUsersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(2, output.getUsers().size()); // ✅ getUsers() (không phải getItems)
    }

    // ===== TC04 =====
    @Test
    void should_filter_users_by_keyword() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "admin");
        useCase.execute(input);

        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());

        ListUsersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(1, output.getUsers().size());
        assertEquals("admin@gmail.com", output.getUsers().get(0).email);
    }

    // ===== TC05 =====
    @Test
    void should_return_empty_list_when_keyword_not_match() {
        when(userRepository.findAll()).thenReturn(mockUsers());

        ListUsersInputData input = ListUsersInputData.forAdmin(true, "notfound");
        useCase.execute(input);

        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());

        ListUsersOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(0, output.getUsers().size());
    }

    // ===== TC06 =====
    @Test
    void should_return_system_error_when_repository_throw_exception() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        ListUsersInputData input = ListUsersInputData.forAdmin(true, null);
        useCase.execute(input);

        ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
        verify(outputBoundary).present(captor.capture());

        ListUsersOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
    }
// ===== TC07: keyword có khoảng trắng -> trim vẫn lọc được =====
@Test
void should_trim_keyword_before_filtering() {
    when(userRepository.findAll()).thenReturn(mockUsers());

    ListUsersInputData input = ListUsersInputData.forAdmin(true, "   admin   ");
    useCase.execute(input);

    ArgumentCaptor<ListUsersOutputData> captor = ArgumentCaptor.forClass(ListUsersOutputData.class);
    verify(outputBoundary).present(captor.capture());

    ListUsersOutputData output = captor.getValue();
    assertTrue(output.isSuccess());
    assertEquals(1, output.getUsers().size());
    assertEquals("admin@gmail.com", output.getUsers().get(0).email);
}

    // ===== Test data =====
    private List<TaiKhoan> mockUsers() {
        LocalDateTime now = LocalDateTime.now();

        TaiKhoan admin = new TaiKhoan(
                1L,
                "admin@gmail.com",
                "admin",
                "pw",           // matKhau
                "0900000000",   // soDienThoai
                "HCM",          // diaChi
                null,           // vaiTro (để null khỏi phụ thuộc enum VaiTro)
                true,           // hoatDong
                now,            // ngayTao
                now,            // ngayCapNhat
                now             // lanDangNhapCuoi
        );

        TaiKhoan user = new TaiKhoan(
                2L,
                "user@gmail.com",
                "user",
                "pw",
                "0911111111",
                "HN",
                null,
                true,
                now,
                now,
                now
        );

        return List.of(admin, user);
    }
}
