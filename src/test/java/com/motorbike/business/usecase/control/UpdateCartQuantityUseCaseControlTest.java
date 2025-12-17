package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.UpdateCartQuantityPresenter;
import com.motorbike.adapters.viewmodels.UpdateCartQuantityViewModel;
import com.motorbike.business.dto.updatecart.UpdateCartQuantityInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.output.UpdateCartQuantityOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;

public class UpdateCartQuantityUseCaseControlTest {

	@Test
	public void testExecute_ValidUpdate_IncreaseQuantity() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, 5);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(5, viewModel.newQuantity);
		assertEquals(false, viewModel.itemRemoved);
	}
	
	@Test
	public void testExecute_ValidUpdate_DecreaseQuantity() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, 1);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(1, viewModel.newQuantity);
	}
	
	@Test
	public void testExecute_ValidUpdate_RemoveItem() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, 0);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(true, viewModel.itemRemoved);
		assertEquals(0, viewModel.newQuantity);
	}
	
	@Test
	public void testExecute_NullInputData() {
		UpdateCartQuantityInputData inputData = null;
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NullCartId() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(null, 1L, 5);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NullProductId() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, null, 5);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NegativeQuantity() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, -5);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_CartNotFound() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(999L, 1L, 5);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_ProductNotInCart() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 888L, 5);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_EdgeCase_UpdateToOne() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, 1);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(1, viewModel.newQuantity);
	}
	
	@Test
	public void testExecute_EdgeCase_UpdateToLargeQuantity() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, 100);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(100, viewModel.newQuantity);
	}
	
	@Test
	public void testExecute_EdgeCase_UpdateSameQuantity() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(1L, 1L, 2);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(2, viewModel.newQuantity);
	}
	
	@Test
	public void testExecute_EdgeCase_MultipleItemsInCart() {
		UpdateCartQuantityInputData inputData = new UpdateCartQuantityInputData(2L, 1L, 10);
		
		CartRepository cartRepo = new MockCartRepository();
		
		UpdateCartQuantityViewModel viewModel = new UpdateCartQuantityViewModel();
		UpdateCartQuantityOutputBoundary outputBoundary = new UpdateCartQuantityPresenter(viewModel);
		
		UpdateCartQuantityUseCaseControl control = new UpdateCartQuantityUseCaseControl(
			outputBoundary, cartRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(10, viewModel.newQuantity);
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
			if (id == null || id == 999L) {
				return Optional.empty();
			}
			
			GioHang cart = new GioHang(100L);
			cart.setMaGioHang(id);
			
			if (id == 2L) {
				ChiTietGioHang item1 = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 2);
				ChiTietGioHang item2 = new ChiTietGioHang(2L, "Yamaha Exciter", new BigDecimal("50000000"), 1);
				cart.themSanPham(item1);
				cart.themSanPham(item2);
			} else {
				ChiTietGioHang item = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 2);
				cart.themSanPham(item);
			}
			
			return Optional.of(cart);
		}
		
		@Override
		public void delete(Long cartId) {
		}
		
		@Override
		public int mergeGuestCartToUserCart(Long guestCartId, Long userCartId) {
			return 0;
		}

		@Override
		public Optional<GioHang> findByUserIdAndProductId(Long userId, Long productId) {
			return Optional.empty();
		}

		@Override
		public void deleteAllByUserId(Long userId) {
		}

		@Override
		public void deleteById(Long cartId) {
		}
	}
}