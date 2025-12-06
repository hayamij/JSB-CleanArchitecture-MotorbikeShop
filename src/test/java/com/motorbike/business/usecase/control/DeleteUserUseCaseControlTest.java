package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.DeleteUserPresenter;
import com.motorbike.adapters.viewmodels.DeleteUserViewModel;
import com.motorbike.business.dto.user.DeleteUserInputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;

public class DeleteUserUseCaseControlTest {

	@Test
	public void testExecute_ValidDelete_Success() {
		DeleteUserInputData inputData = new DeleteUserInputData(1L);
		
		UserRepository userRepo = new MockUserRepository();
		DeleteUserViewModel viewModel = new DeleteUserViewModel();
		DeleteUserOutputBoundary outputBoundary = new DeleteUserPresenter(viewModel);
		
		DeleteUserUseCaseControl control = new DeleteUserUseCaseControl(outputBoundary, userRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
	}

	@Test
	public void testExecute_UserNotFound() {
		DeleteUserInputData inputData = new DeleteUserInputData(999L);
		
		UserRepository userRepo = new MockUserRepository();
		DeleteUserViewModel viewModel = new DeleteUserViewModel();
		DeleteUserOutputBoundary outputBoundary = new DeleteUserPresenter(viewModel);
		
		DeleteUserUseCaseControl control = new DeleteUserUseCaseControl(outputBoundary, userRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	@Test
	public void testExecute_NullInputData() {
		UserRepository userRepo = new MockUserRepository();
		DeleteUserViewModel viewModel = new DeleteUserViewModel();
		DeleteUserOutputBoundary outputBoundary = new DeleteUserPresenter(viewModel);
		
		DeleteUserUseCaseControl control = new DeleteUserUseCaseControl(outputBoundary, userRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	private static class MockUserRepository implements UserRepository {
		@Override
		public Optional<TaiKhoan> findByEmail(String email) {
			return Optional.empty();
		}

		@Override
		public Optional<TaiKhoan> findByUsernameOrEmailOrPhone(String username) {
			return findByEmail(username);
		}

		@Override
		public Optional<TaiKhoan> findById(Long id) {
			if (id == 1L) {
				return Optional.of(new TaiKhoan(
					1L, "Test User", "user@test.com", "user", "password", "0912345678",
					"123 Street", VaiTro.CUSTOMER, true,
					LocalDateTime.now(), LocalDateTime.now(), null
				));
			}
			return Optional.empty();
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
		public TaiKhoan save(TaiKhoan taiKhoan) {
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
			return userId == 1L;
		}

		@Override
		public java.util.List<TaiKhoan> searchUsers(String keyword) {
			return new java.util.ArrayList<>();
		}
	}
}
