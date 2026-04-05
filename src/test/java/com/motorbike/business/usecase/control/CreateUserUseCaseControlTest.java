package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.CreateUserPresenter;
import com.motorbike.adapters.viewmodels.CreateUserViewModel;
import com.motorbike.business.dto.user.AddUserInputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.CreateUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;
import com.motorbike.domain.exceptions.ValidationException;

public class CreateUserUseCaseControlTest {

	@Test
	public void testExecute_ValidUser_Success() {
		AddUserInputData inputData = new AddUserInputData(
			"Nguyễn Văn Test",
			"newuser@test.com",
			"newuser",
			"password123",
			"0912345678",
			"123 Street",
			"CUSTOMER"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CreateUserViewModel viewModel = new CreateUserViewModel();
		CreateUserOutputBoundary outputBoundary = new CreateUserPresenter(viewModel);
		
		CreateUserUseCaseControl control = new CreateUserUseCaseControl(outputBoundary, userRepo);
		control.execute(inputData);
		
		// Debug: Print error if failed
		if (!viewModel.success) {
			System.out.println("ERROR: " + viewModel.errorCode + " - " + viewModel.errorMessage);
		}
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.maTaiKhoan);
	}

	@Test
	public void testExecute_DuplicateEmail() {
		AddUserInputData inputData = new AddUserInputData(
			"Trần Văn B",
			"existing@test.com",
			"user",
			"password123",
			"0912345678",
			"123 Street",
			"CUSTOMER"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CreateUserViewModel viewModel = new CreateUserViewModel();
		CreateUserOutputBoundary outputBoundary = new CreateUserPresenter(viewModel);
		
		CreateUserUseCaseControl control = new CreateUserUseCaseControl(outputBoundary, userRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	@Test
	public void testExecute_NullInputData() {
		UserRepository userRepo = new MockUserRepository();
		CreateUserViewModel viewModel = new CreateUserViewModel();
		CreateUserOutputBoundary outputBoundary = new CreateUserPresenter(viewModel);
		
		CreateUserUseCaseControl control = new CreateUserUseCaseControl(outputBoundary, userRepo);
		
		assertThrows(ValidationException.class, () -> control.execute(null));
	}

	private static class MockUserRepository implements UserRepository {
		private Long nextId = 1L;

	@Override
	public Optional<TaiKhoan> findByEmail(String email) {
		if ("existing@test.com".equals(email)) {
			LocalDateTime now = LocalDateTime.now();
			return Optional.of(new TaiKhoan(
				1L,
				"Existing User",
				"existing@test.com",
				"existing",
				"password",
				"0912345678",
				"123 Street",
				VaiTro.CUSTOMER,
				true,
				now,
				now,
				null
			));
		}
		return Optional.empty();
	}

	@Override
	public Optional<TaiKhoan> findByUsernameOrEmailOrPhone(String username) {
		return findByEmail(username);
	}

	@Override
	public boolean existsByTenDangNhap(String tenDangNhap) {
		return false;
	}

	@Override
	public Optional<TaiKhoan> findById(Long id) {
		return Optional.empty();
	}		@Override
		public boolean existsByEmail(String email) {
			return "existing@test.com".equals(email);
		}

		@Override
		public boolean existsByUsername(String username) {
			return false;
		}

		@Override
		public TaiKhoan save(TaiKhoan taiKhoan) {
			if (taiKhoan.getMaTaiKhoan() == null) {
				taiKhoan.setMaTaiKhoan(nextId++);
			}
			return taiKhoan;
		}

		@Override
		public void updateLastLogin(Long userId) {
		}

		@Override
		public java.util.List<TaiKhoan> findAll() {
			return new java.util.ArrayList<>();
		}

		@Override
		public void deleteById(Long userId) {
		}

		@Override
		public boolean existsById(Long userId) {
			return false;
		}

		@Override
		public java.util.List<TaiKhoan> searchUsers(String keyword) {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public Optional<com.motorbike.domain.entities.TaiKhoan> findByTenDangNhap(String tenDangNhap) {
			return Optional.empty();
		}
	}
}
