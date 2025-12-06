package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.RegisterPresenter;
import com.motorbike.adapters.viewmodels.RegisterViewModel;
import com.motorbike.business.dto.register.RegisterInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.RegisterOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.TaiKhoan;

public class RegisterUseCaseControlTest {

	@Test
	public void testExecute_ValidRegistration_Success() {
		RegisterInputData inputData = new RegisterInputData(
			"New User",
			"newuser@test.com",
			"newuser",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.userId);
		assertEquals("newuser@test.com", viewModel.email);
	}
	
	@Test
	public void testExecute_ValidRegistration_WithAddress() {
		RegisterInputData inputData = new RegisterInputData(
			"User Two",
			"user2@test.com",
			"user2",
			"password123",
			"password123",
			"0912345678",
			"123 Main Street"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_ValidRegistration_MinimalData() {
		RegisterInputData inputData = new RegisterInputData(
			"Mi",
			"min@test.com",
			"usr",
			"pass12",
			"pass12",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullInputData() {
		RegisterInputData inputData = null;
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyEmail() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"",
			"username",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullEmail() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			null,
			"username",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyUsername() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullPassword() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"username",
			null,
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_PasswordMismatch() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"username",
			"12345",
			"0912345678",
			"Hanoi"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmailAlreadyExists() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"existing@test.com",
			"username",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_ShortUsername() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"ab",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_ShortPassword() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"username",
			"pass",
			"pass",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_InvalidPhoneFormat() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"username",
			"password123",
			"password123",
			"invalid"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_LongUsername() {
		String longUsername = "a".repeat(100);
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			longUsername,
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_ValidMinimalUsername() {
		RegisterInputData inputData = new RegisterInputData(
			"Test User",
			"user@test.com",
			"abc",
			"password123",
			"password123",
			"0912345678"
		);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		RegisterViewModel viewModel = new RegisterViewModel();
		RegisterOutputBoundary outputBoundary = new RegisterPresenter(viewModel);
		
		RegisterUseCaseControl control = new RegisterUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	private static class MockUserRepository implements UserRepository {
		private Long nextId = 1L;
		
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
	}		@Override
		public boolean existsByEmail(String email) {
			return email != null && email.equals("existing@test.com");
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
	}
	
	private static class MockCartRepository implements CartRepository {
		@Override
		public Optional<GioHang> findByUserId(Long userId) {
			return Optional.empty();
		}
		
		@Override
		public GioHang save(GioHang gioHang) {
			if (gioHang.getMaGioHang() == null) {
				gioHang.setMaGioHang(1L);
			}
			return gioHang;
		}
		
		@Override
		public Optional<GioHang> findById(Long id) {
			return Optional.empty();
		}
		
		@Override
		public void delete(Long cartId) {
		}
		
		@Override
		public int mergeGuestCartToUserCart(Long guestCartId, Long userCartId) {
			return 0;
		}
	}
}
