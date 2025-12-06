package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.LoginPresenter;
import com.motorbike.adapters.viewmodels.LoginViewModel;
import com.motorbike.business.dto.login.LoginInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.LoginOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.TaiKhoan;
import com.motorbike.domain.entities.VaiTro;

public class LoginUseCaseControlTest {

	@Test
	public void testExecute_ValidCredentials_Success() {
		LoginInputData inputData = new LoginInputData("user@test.com", "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.userId);
		assertEquals("user@test.com", viewModel.email);
	}
	
	@Test
	public void testExecute_ValidCredentials_WithGuestCart() {
		LoginInputData inputData = new LoginInputData("user@test.com", "password123", 999L);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertEquals(true, viewModel.cartMerged);
	}
	
	@Test
	public void testExecute_ValidCredentials_AdminRole() {
		LoginInputData inputData = new LoginInputData("admin@test.com", "admin123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullInputData() {
		LoginInputData inputData = null;
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyEmail() {
		LoginInputData inputData = new LoginInputData("", "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullEmail() {
		LoginInputData inputData = new LoginInputData(null, "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyPassword() {
		LoginInputData inputData = new LoginInputData("user@test.com", "");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullPassword() {
		LoginInputData inputData = new LoginInputData("user@test.com", null);
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_LoginWithUsername() {
		LoginInputData inputData = new LoginInputData("notanemail", "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_UserNotFound() {
		LoginInputData inputData = new LoginInputData("notfound@test.com", "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_WrongPassword() {
		LoginInputData inputData = new LoginInputData("user@test.com", "wrongpassword");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_AccountLocked() {
		LoginInputData inputData = new LoginInputData("locked@test.com", "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_ValidMinimalEmail() {
		LoginInputData inputData = new LoginInputData("ab@cd.ef", "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_LongEmail() {
		String longEmail = "verylongemailaddress" + "x".repeat(50) + "@test.com";
		LoginInputData inputData = new LoginInputData(longEmail, "password123");
		
		UserRepository userRepo = new MockUserRepository();
		CartRepository cartRepo = new MockCartRepository();
		
		LoginViewModel viewModel = new LoginViewModel();
		LoginOutputBoundary outputBoundary = new LoginPresenter(viewModel);
		
		LoginUseCaseControl control = new LoginUseCaseControl(outputBoundary, userRepo, cartRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	private static class MockUserRepository implements UserRepository {
		@Override
		public Optional<TaiKhoan> findByEmail(String email) {
			if (email == null || email.equals("notfound@test.com")) {
				return Optional.empty();
			}
			
			if (email.equals("locked@test.com")) {
				TaiKhoan lockedAccount = new TaiKhoan(
					3L, "Locked User", email, "lockeduser", "password123",
					"0912345678", "123 Street", VaiTro.CUSTOMER,
					false, LocalDateTime.now(), LocalDateTime.now(), null
				);
				return Optional.of(lockedAccount);
			}
			
			if (email.equals("admin@test.com")) {
				TaiKhoan admin = new TaiKhoan(
					2L, "Admin User", email, "admin", "admin123",
					"0912345678", "123 Street", VaiTro.ADMIN,
					true, LocalDateTime.now(), LocalDateTime.now(), null
				);
				return Optional.of(admin);
			}
			
			TaiKhoan user = new TaiKhoan(
				1L, "Test User", email, "testuser", "password123",
				"0912345678", "123 Street", VaiTro.CUSTOMER,
				true, LocalDateTime.now(), LocalDateTime.now(), null
			);
			return Optional.of(user);
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
			return !email.equals("notfound@test.com");
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
			GioHang cart = new GioHang(userId);
			cart.setMaGioHang(1L);
			return Optional.of(cart);
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
			if (id == 999L) {
				GioHang guestCart = new GioHang(null);
				guestCart.setMaGioHang(999L);
				return Optional.of(guestCart);
			}
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
