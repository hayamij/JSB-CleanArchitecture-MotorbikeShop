package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationInputData;
import com.motorbike.business.dto.checkuserduplication.CheckUserDuplicationOutputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckUserDuplicationUseCaseControlTest {

    @Test
    void shouldDetectDuplicateUsername() {
        // Given
        String username = "john_doe";
        String email = "john@example.com";
        TaiKhoan existingUser = new TaiKhoan("John Doe", email, username, "password", "0912345678", "123 Street");
        existingUser.setMaTaiKhoan(1L);

        UserRepository userRepo = new MockUserRepository(existingUser);
        CheckUserDuplicationUseCaseControl useCase = new CheckUserDuplicationUseCaseControl(null, userRepo);

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData("new@example.com", username, null);

        // When
        CheckUserDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertTrue(outputData.isDuplicate());
        assertEquals("username", outputData.getDuplicatedField());
        assertEquals(1L, outputData.getExistingUserId());
    }

    @Test
    void shouldDetectDuplicateEmail() {
        // Given
        String email = "john@example.com";
        TaiKhoan existingUser = new TaiKhoan("Other User", email, "other_user", "password", "0912345678", "456 Street");
        existingUser.setMaTaiKhoan(2L);

        UserRepository userRepo = new MockUserRepository(existingUser);
        CheckUserDuplicationUseCaseControl useCase = new CheckUserDuplicationUseCaseControl(null, userRepo);

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData(email, "new_user", null);

        // When
        CheckUserDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertTrue(outputData.isDuplicate());
        assertEquals("email", outputData.getDuplicatedField());
    }

    @Test
    void shouldAllowUniqueUser() {
        // Given
        String username = "new_user";
        String email = "new@example.com";

        UserRepository userRepo = new MockUserRepository(null);
        CheckUserDuplicationUseCaseControl useCase = new CheckUserDuplicationUseCaseControl(null, userRepo);

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData(email, username, null);

        // When
        CheckUserDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertFalse(outputData.isDuplicate());
    }

    @Test
    void shouldAllowSameUserForUpdate() {
        // Given
        Long userId = 1L;
        String email = "john@example.com";
        TaiKhoan existingUser = new TaiKhoan("John Doe", email, "john_doe", "password", "0912345678", "789 Street");
        existingUser.setMaTaiKhoan(userId);

        UserRepository userRepo = new MockUserRepository(existingUser);
        CheckUserDuplicationUseCaseControl useCase = new CheckUserDuplicationUseCaseControl(null, userRepo);

        CheckUserDuplicationInputData inputData = new CheckUserDuplicationInputData(email, "john_doe", userId);

        // When
        CheckUserDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertFalse(outputData.isDuplicate());
    }

    private static class MockUserRepository implements UserRepository {
        private final TaiKhoan user;

        public MockUserRepository(TaiKhoan user) {
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
            return taiKhoan;
        }

        @Override
        public Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap) {
            if (user != null && user.getTenDangNhap().equals(tenDangNhap)) {
                return Optional.of(user);
            }
            return Optional.empty();
        }

        @Override
        public Optional<TaiKhoan> findByEmail(String email) {
            if (user != null && user.getEmail().equals(email)) {
                return Optional.of(user);
            }
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
        public boolean existsByUsername(String username) {
            return false;
        }
        
        @Override
        public Optional<TaiKhoan> findByUsernameOrEmailOrPhone(String username) {
            return Optional.empty();
        }

        @Override
        public java.util.List<TaiKhoan> findAll() {
            return new java.util.ArrayList<>();
        }

        @Override
        public void deleteById(Long id) {
        }
        
        @Override
        public java.util.List<TaiKhoan> searchUsers(String keyword) {
            return new java.util.ArrayList<>();
        }
        
        @Override
        public boolean existsById(Long id) {
            return user != null && user.getMaTaiKhoan().equals(id);
        }
        
        @Override
        public void updateLastLogin(Long userId) {
        }
    }
}
