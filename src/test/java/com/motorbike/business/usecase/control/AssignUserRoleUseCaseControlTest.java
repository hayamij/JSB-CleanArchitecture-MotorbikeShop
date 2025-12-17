package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.assignuserrole.AssignUserRoleInputData;
import com.motorbike.business.dto.assignuserrole.AssignUserRoleOutputData;
import com.motorbike.business.ports.repository.TaiKhoanRepository;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssignUserRoleUseCaseControlTest {

    @Test
    void shouldAssignAdminRoleSuccessfully() {
        // Given
        TaiKhoan user = new TaiKhoan("Test User", "test@example.com", "testuser", "Test@123", "0912345678", "123 Street");
        user.setMaTaiKhoan(1L);

        TaiKhoanRepository taiKhoanRepo = new MockTaiKhoanRepository(user);
        AssignUserRoleUseCaseControl useCase = new AssignUserRoleUseCaseControl(null, taiKhoanRepo);

        AssignUserRoleInputData inputData = new AssignUserRoleInputData(1L, "ADMIN");

        // When
        AssignUserRoleOutputData outputData = useCase.assignInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
    }

    @Test
    void shouldAssignCustomerRoleSuccessfully() {
        // Given
        TaiKhoan user = new TaiKhoan("Test User", "test@example.com", "testuser", "Test@123", "0912345678", "123 Street");
        user.setMaTaiKhoan(1L);
        user.setVaiTro(VaiTro.ADMIN);

        TaiKhoanRepository taiKhoanRepo = new MockTaiKhoanRepository(user);
        AssignUserRoleUseCaseControl useCase = new AssignUserRoleUseCaseControl(null, taiKhoanRepo);

        AssignUserRoleInputData inputData = new AssignUserRoleInputData(1L, "CUSTOMER");

        // When
        AssignUserRoleOutputData outputData = useCase.assignInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
    }

    @Test
    void shouldFailWhenUserNotFound() {
        // Given
        TaiKhoanRepository taiKhoanRepo = new MockTaiKhoanRepository(null);
        AssignUserRoleUseCaseControl useCase = new AssignUserRoleUseCaseControl(null, taiKhoanRepo);

        AssignUserRoleInputData inputData = new AssignUserRoleInputData(999L, "ADMIN");

        // When
        AssignUserRoleOutputData outputData = useCase.assignInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
    }

    private static class MockTaiKhoanRepository implements TaiKhoanRepository {
        private TaiKhoan user;

        public MockTaiKhoanRepository(TaiKhoan user) {
            this.user = user;
        }

        @Override
        public Optional<TaiKhoan> findById(Long id) {
            if (user != null && user.getMaTaiKhoan().equals(id)) {
                return Optional.of(user);
            }
            return Optional.empty();
        }

        @Override
        public TaiKhoan save(TaiKhoan taiKhoan) {
            this.user = taiKhoan;
            return taiKhoan;
        }

        @Override
        public Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap) {
            return Optional.empty();
        }

        @Override
        public Optional<TaiKhoan> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public boolean existsByTenDangNhap(String tenDangNhap) {
            return false;
        }

        @Override
        public boolean existsByEmail(String email) {
            return false;
        }

        @Override
        public java.util.List<TaiKhoan> findAll() {
            return new java.util.ArrayList<>();
        }

        @Override
        public void deleteById(Long id) {
        }
        
        @Override
        public boolean existsById(Long id) {
            return user != null && user.getMaTaiKhoan().equals(id);
        }
    }
}
