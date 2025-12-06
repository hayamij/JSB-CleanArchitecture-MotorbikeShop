package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetAllUsersPresenter;
import com.motorbike.adapters.viewmodels.GetAllUsersViewModel;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.GetAllUsersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;

public class GetAllUsersUseCaseControlTest {

	@Test
	public void testExecute_WithUsers_Success() {
		UserRepository userRepo = new MockUserRepositoryWithData();
		GetAllUsersViewModel viewModel = new GetAllUsersViewModel();
		GetAllUsersOutputBoundary outputBoundary = new GetAllUsersPresenter(viewModel);
		
		GetAllUsersUseCaseControl control = new GetAllUsersUseCaseControl(outputBoundary, userRepo);
		control.execute(null);
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.users);
		assertEquals(2, viewModel.users.size());
	}

	@Test
	public void testExecute_EmptyList() {
		UserRepository userRepo = new MockUserRepositoryEmpty();
		GetAllUsersViewModel viewModel = new GetAllUsersViewModel();
		GetAllUsersOutputBoundary outputBoundary = new GetAllUsersPresenter(viewModel);
		
		GetAllUsersUseCaseControl control = new GetAllUsersUseCaseControl(outputBoundary, userRepo);
		control.execute(null);
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.users);
		assertEquals(0, viewModel.users.size());
	}

	private static class MockUserRepositoryWithData implements UserRepository {
		@Override
		public List<TaiKhoan> findAll() {
			List<TaiKhoan> users = new ArrayList<>();
			users.add(new TaiKhoan(
				1L, "Admin User", "admin@test.com", "admin", "password", "0912345678",
				"123 Admin Street", VaiTro.ADMIN, true,
				LocalDateTime.now(), LocalDateTime.now(), null
			));
			users.add(new TaiKhoan(
				2L, "Customer User", "customer@test.com", "customer", "password", "0987654321",
				"456 Customer Street", VaiTro.CUSTOMER, true,
				LocalDateTime.now(), LocalDateTime.now(), null
			));
			return users;
		}

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
		public void deleteById(Long userId) {
		}

		@Override
		public boolean existsById(Long userId) {
			return false;
		}

		@Override
		public List<TaiKhoan> searchUsers(String keyword) {
			return new ArrayList<>();
		}
	}

	private static class MockUserRepositoryEmpty implements UserRepository {
		@Override
		public List<TaiKhoan> findAll() {
			return new ArrayList<>();
		}

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
		public void deleteById(Long userId) {
		}

		@Override
		public boolean existsById(Long userId) {
			return false;
		}

		@Override
		public List<TaiKhoan> searchUsers(String keyword) {
			return new ArrayList<>();
		}
	}
}
