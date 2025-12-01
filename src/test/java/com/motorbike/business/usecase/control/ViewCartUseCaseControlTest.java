package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.ViewCartPresenter;
import com.motorbike.adapters.viewmodels.ViewCartViewModel;
import com.motorbike.business.dto.viewcart.ViewCartInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.ViewCartOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;

public class ViewCartUseCaseControlTest {

	@Test
	public void testExecute_ValidUserId_CartWithItems() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.isEmpty);
		assertNotEquals(null, viewModel.cartId);
		assertEquals(1, viewModel.totalItems);
	}
	
	@Test
	public void testExecute_ValidUserId_MultipleItems() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(200L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.isEmpty);
		assertEquals(2, viewModel.totalItems);
	}
	
	@Test
	public void testExecute_ValidUserId_LargeCart() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(300L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.isEmpty);
	}
	
	@Test
	public void testExecute_NullInputData() {
		ViewCartInputData inputData = null;
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorMessage);
	}
	
	@Test
	public void testExecute_NullUserId() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(null);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertNotEquals(null, viewModel.errorMessage);
	}
	
	@Test
	public void testExecute_EmptyCart() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(999L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.isEmpty);
	}
	
	@Test
	public void testExecute_CartNotFound() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(888L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.isEmpty);
	}
	
	@Test
	public void testExecute_EdgeCase_CartWithOneItem() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(1, viewModel.totalItems);
	}
	
	@Test
	public void testExecute_EdgeCase_CartWithMaxQuantity() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(400L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.isEmpty);
	}
	
	@Test
	public void testExecute_EdgeCase_MinimalUserId() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(1L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
	}
	
	@Test
	public void testExecute_EdgeCase_LargeUserId() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(Long.MAX_VALUE);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.isEmpty);
	}
	
	@Test
	public void testExecute_ValidCart_CheckTotalAmount() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertNotEquals(null, viewModel.formattedTotalAmount);
	}
	
	@Test
	public void testExecute_ValidCart_CheckCartId() {
		ViewCartInputData inputData = ViewCartInputData.forLoggedInUser(100L);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		ViewCartViewModel viewModel = new ViewCartViewModel();
		ViewCartOutputBoundary outputBoundary = new ViewCartPresenter(viewModel);
		
		ViewCartUseCaseControl control = new ViewCartUseCaseControl(outputBoundary, cartRepo, productRepo);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertNotEquals(null, viewModel.cartId);
	}
	
	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long productId) {
			if (productId == null) {
				return Optional.empty();
			}
			
			XeMay product = new XeMay(
				"Test Product " + productId,
				"Test product description",
				new BigDecimal("30000000"),
				"/images/test-product.jpg",
				10,
				"Honda",
				"Wave",
				"Red",
				2024,
				110
			);
			
			return Optional.of(product);
		}
		
		@Override
		public SanPham save(SanPham sanPham) {
			if (sanPham.getMaSanPham() == null) {
				sanPham.setMaSanPham(1L);
			}
			return sanPham;
		}
		
		@Override
		public boolean existsById(Long productId) {
			return productId != null;
		}
		
		@Override
		public List<SanPham> findAll() {
			return Collections.emptyList();
		}
	}
	
	private static class MockCartRepository implements CartRepository {
		@Override
		public Optional<GioHang> findByUserId(Long userId) {
			if (userId == null || userId == 888L || userId == Long.MAX_VALUE) {
				return Optional.empty();
			}
			
			GioHang cart = new GioHang(userId);
			cart.setMaGioHang(1L);
			
			if (userId == 999L) {
				return Optional.of(cart);
			}
			
			if (userId == 200L) {
				ChiTietGioHang item1 = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 2);
				ChiTietGioHang item2 = new ChiTietGioHang(2L, "Yamaha Exciter", new BigDecimal("50000000"), 1);
				cart.themSanPham(item1);
				cart.themSanPham(item2);
			} else if (userId == 300L) {
				for (int i = 1; i <= 5; i++) {
					ChiTietGioHang item = new ChiTietGioHang((long)i, "Product " + i, new BigDecimal("10000000"), 1);
					cart.themSanPham(item);
				}
			} else if (userId == 400L) {
				ChiTietGioHang item = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 100);
				cart.themSanPham(item);
			} else {
				ChiTietGioHang item = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 2);
				cart.themSanPham(item);
			}
			
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
