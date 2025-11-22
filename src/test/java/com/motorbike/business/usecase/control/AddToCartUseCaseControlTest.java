package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.AddToCartPresenter;
import com.motorbike.adapters.viewmodels.AddToCartViewModel;
import com.motorbike.business.dto.addtocart.AddToCartInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.AddToCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.XeMay;

public class AddToCartUseCaseControlTest {

	@Test
	public void testExecute_ValidInput_LoggedInUser_Success() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 2, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.cartId);
		assertEquals(1, viewModel.totalItems);
	}
	
	@Test
	public void testExecute_ValidInput_MinimumQuantity() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 1, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_ValidInput_LargeQuantity() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 50, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullInputData() {
		AddToCartInputData inputData = null;
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NullUserId() {
		AddToCartInputData inputData = new AddToCartInputData(1L, 2, null, null);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NullProductId() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(null, 2, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_ZeroQuantity() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 0, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NegativeQuantity() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, -5, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_ProductNotFound() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(999L, 2, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_InsufficientStock() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 200, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_EdgeCase_ExactStockQuantity() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 100, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_StockMinusOne() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 99, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_StockPlusOne() {
		AddToCartInputData inputData = AddToCartInputData.forLoggedInUser(1L, 101, 100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		AddToCartViewModel viewModel = new AddToCartViewModel();
		AddToCartOutputBoundary outputBoundary = new AddToCartPresenter(viewModel);
		
		AddToCartUseCaseControl control = new AddToCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	private static class MockCartRepository implements CartRepository {
		@Override
		public Optional<GioHang> findByUserId(Long userId) {
			if (userId == null) return Optional.empty();
			return Optional.of(new GioHang(userId));
		}
		
		@Override
		public GioHang save(GioHang gioHang) {
			gioHang.setMaGioHang(1L);
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
	
	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<com.motorbike.domain.entities.SanPham> findById(Long id) {
			if (id == null || id == 999L) {
				return Optional.empty();
			}
			XeMay product = new XeMay(
				"Honda Wave",
				"Xe số tiết kiệm nhiên liệu",
				new BigDecimal("30000000"),
				"honda-wave.jpg",
				100,
				"Honda",
				"Wave Alpha",
				"Đỏ",
				2024,
				110
			);
			product.setMaSanPham(id);
			return Optional.of(product);
		}
		
		@Override
		public com.motorbike.domain.entities.SanPham save(com.motorbike.domain.entities.SanPham product) {
			return product;
		}
		
		@Override
		public boolean existsById(Long productId) {
			return productId != null && productId != 999L;
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.SanPham> findAll() {
			return new java.util.ArrayList<>();
		}
	}
}
