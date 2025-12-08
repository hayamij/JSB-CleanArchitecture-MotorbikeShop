package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.CheckoutPresenter;
import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.business.dto.checkout.CheckoutInputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.entities.XeMay;

public class CheckoutUseCaseControlTest {

	@Test
	public void testExecute_ValidCheckout_Success() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"Nguyen Van A",
			"0912345678",
			"123 Main St",
			"Giao trong gio hanh chinh"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.orderId);
		assertEquals(100L, viewModel.customerId);
	}
	
	@Test
	public void testExecute_ValidCheckout_WithNote() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"Tran Thi B",
			"0987654321",
			"456 Side St",
			"Goi truoc khi giao"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_ValidCheckout_NoNote() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"Le Van C",
			"0901234567",
			"789 Park Ave",
			null
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullInputData() {
		CheckoutInputData inputData = null;
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NullUserId() {
		CheckoutInputData inputData = new CheckoutInputData(
			null,
			"Nguyen Van A",
			"0912345678",
			"123 Main St",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyCart() {
		CheckoutInputData inputData = new CheckoutInputData(
			999L,
			"Nguyen Van A",
			"0912345678",
			"123 Main St",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_CartNotFound() {
		CheckoutInputData inputData = new CheckoutInputData(
			888L,
			"Nguyen Van A",
			"0912345678",
			"123 Main St",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_InsufficientStock() {
		CheckoutInputData inputData = new CheckoutInputData(
			200L,
			"Nguyen Van A",
			"0912345678",
			"123 Main St",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyReceiverName() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"",
			"0912345678",
			"123 Main St",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyPhoneNumber() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"Nguyen Van A",
			"",
			"123 Main St",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EmptyShippingAddress() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"Nguyen Van A",
			"0912345678",
			"",
			"Note"
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_MinimalValidData() {
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			"A",
			"1",
			"X",
			""
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_LongStrings() {
		String longName = "A".repeat(200);
		String longPhone = "0".repeat(20);
		String longAddress = "X".repeat(500);
		
		CheckoutInputData inputData = new CheckoutInputData(
			100L,
			longName,
			longPhone,
			longAddress,
			null
		);
		
		CartRepository cartRepo = new MockCartRepository();
		ProductRepository productRepo = new MockProductRepository();
		OrderRepository orderRepo = new MockOrderRepository();
		
		CheckoutViewModel viewModel = new CheckoutViewModel();
		CheckoutOutputBoundary outputBoundary = new CheckoutPresenter(viewModel);
		
		CheckoutUseCaseControl control = new CheckoutUseCaseControl(
			outputBoundary, cartRepo, productRepo, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}
	
	private static class MockCartRepository implements CartRepository {
		@Override
		public Optional<GioHang> findByUserId(Long userId) {
			if (userId == null || userId == 888L) {
				return Optional.empty();
			}
			
			GioHang cart = new GioHang(userId);
			
			if (userId == 999L) {
				return Optional.of(cart);
			}
			
			if (userId == 200L) {
				ChiTietGioHang item = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 150);
				cart.themSanPham(item);
			} else {
				ChiTietGioHang item = new ChiTietGioHang(1L, "Honda Wave", new BigDecimal("30000000"), 2);
				cart.themSanPham(item);
			}
			
			return Optional.of(cart);
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
			if (id == null) {
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
			return productId != null;
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.SanPham> findAll() {
			return new java.util.ArrayList<>();
		}
	}
	
	private static class MockOrderRepository implements OrderRepository {
		private Long nextId = 1L;
		
		@Override
		public DonHang save(DonHang donHang) {
			donHang.setMaDonHang(nextId++);
			return donHang;
		}
		
		@Override
		public Optional<DonHang> findById(Long orderId) {
			return Optional.empty();
		}
		
		@Override
		public List<DonHang> findByUserId(Long userId) {
			return new ArrayList<>();
		}
		
		@Override
		public List<DonHang> findByStatus(TrangThaiDonHang trangThai) {
			return new ArrayList<>();
		}
		
		@Override
		public List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai) {
			return new ArrayList<>();
		}
		
		@Override
		public List<DonHang> findAll() {
			return new ArrayList<>();
		}

		@Override
		public List<DonHang> searchForAdmin(String keyword) {
			return new ArrayList<>();
		}
		
		@Override
		public void deleteById(Long orderId) {
		}
		
		@Override
		public boolean existsById(Long orderId) {
			return false;
		}
	}
}
