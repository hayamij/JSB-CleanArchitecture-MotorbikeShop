package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.SearchUsersPresenter;
import com.motorbike.adapters.viewmodels.SearchUsersViewModel;
import com.motorbike.business.dto.user.SearchUsersInputData;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.SearchUsersOutputBoundary;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;

public class SearchUsersUseCaseControlTest {

	@Test
	public void testExecute_WithKeyword_Success() {
		SearchUsersInputData inputData = new SearchUsersInputData("admin", null, null);
		
		UserRepository userRepo = new MockUserRepository();
		SearchUsersViewModel viewModel = new SearchUsersViewModel();
		SearchUsersOutputBoundary outputBoundary = new SearchUsersPresenter(viewModel);
		
		SearchUsersUseCaseControl control = new SearchUsersUseCaseControl(outputBoundary, userRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.users);
		assertEquals(1, viewModel.users.size());
	}

	@Test
	public void testExecute_NoResults() {
		SearchUsersInputData inputData = new SearchUsersInputData("xyz123", null, null);
		
		UserRepository userRepo = new MockUserRepository();
		SearchUsersViewModel viewModel = new SearchUsersViewModel();
		SearchUsersOutputBoundary outputBoundary = new SearchUsersPresenter(viewModel);
		
		SearchUsersUseCaseControl control = new SearchUsersUseCaseControl(outputBoundary, userRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.users);
		assertEquals(0, viewModel.users.size());
	}

	@Test
	public void testExecute_NullInputData() {
		UserRepository userRepo = new MockUserRepository();
		SearchUsersViewModel viewModel = new SearchUsersViewModel();
		SearchUsersOutputBoundary outputBoundary = new SearchUsersPresenter(viewModel);
		
		SearchUsersUseCaseControl control = new SearchUsersUseCaseControl(outputBoundary, userRepo);
		control.execute(null);
		
		// Use case catches NullPointerException and returns system error
		assertTrue(viewModel.hasError);
		assertNotNull(viewModel.errorMessage);
	}

	private static class MockUserRepository implements UserRepository {
		@Override
		public List<TaiKhoan> searchUsers(String keyword) {
			List<TaiKhoan> users = new ArrayList<>();
			if ("admin".equalsIgnoreCase(keyword)) {
				users.add(new TaiKhoan(
					1L, "admin@test.com", "admin", "password", "0912345678",
					"123 Admin Street", VaiTro.ADMIN, true,
					LocalDateTime.now(), LocalDateTime.now(), null
				));
			}
			return users;
		}

		@Override
		public Optional<TaiKhoan> findByEmail(String email) {
			return Optional.empty();
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
		public TaiKhoan save(TaiKhoan taiKhoan) {
			return taiKhoan;
		}

		@Override
		public void updateLastLogin(Long userId) {
		}

		@Override
		public List<TaiKhoan> findAll() {
			List<TaiKhoan> users = new ArrayList<>();
			users.add(new TaiKhoan(
				1L, "admin@test.com", "admin", "password", "0912345678",
				"123 Admin Street", VaiTro.ADMIN, true,
				LocalDateTime.now(), LocalDateTime.now(), null
			));
			users.add(new TaiKhoan(
				2L, "customer@test.com", "customer", "password", "0987654321",
				"456 Customer Street", VaiTro.CUSTOMER, true,
				LocalDateTime.now(), LocalDateTime.now(), null
			));
			return users;
		}

		@Override
		public void deleteById(Long userId) {
		}

		@Override
		public boolean existsById(Long userId) {
			return false;
		}
	}
}
