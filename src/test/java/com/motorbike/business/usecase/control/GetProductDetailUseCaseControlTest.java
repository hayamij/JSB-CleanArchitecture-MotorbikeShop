package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.ProductDetailPresenter;
import com.motorbike.adapters.viewmodels.ProductDetailViewModel;
import com.motorbike.business.dto.productdetail.GetProductDetailInputData;
import com.motorbike.business.dto.productdetail.GetProductDetailOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetProductDetailOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;

public class GetProductDetailUseCaseControlTest {

	@Test
	public void testExecute_ValidProductId_Success() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(1L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.productId);
		assertNotEquals(null, viewModel.name);
	}
	
	@Test
	public void testExecute_ValidProductId_WithStock() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(2L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.stockQuantity);
	}
	
	@Test
	public void testExecute_ValidProductId_CheckPrice() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(3L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.formattedPrice);
	}
	
	@Test
	public void testExecute_NullInputData() {
		GetProductDetailInputData inputData = null;
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_NullProductId() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(null);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_ProductNotFound() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(999L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.hasError);
		assertNotEquals(null, viewModel.errorCode);
	}
	
	@Test
	public void testExecute_ZeroProductId() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(0L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_NegativeProductId() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(-1L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_ProductWithZeroStock() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(100L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
		assertNotEquals(null, viewModel.availabilityStatus);
	}
	
	@Test
	public void testExecute_EdgeCase_MinValidProductId() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(1L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_LargeProductId() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(Long.MAX_VALUE);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_ProductWithHighStock() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(200L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
	}
	
	@Test
	public void testExecute_EdgeCase_ProductWithLowStock() {
		GetProductDetailInputData inputData = new GetProductDetailInputData(300L);
		
		ProductRepository productRepo = new MockProductRepository();
		
		ProductDetailViewModel viewModel = new ProductDetailViewModel();
		GetProductDetailOutputBoundary outputBoundary = new ProductDetailPresenter(viewModel);
		
		GetProductDetailUseCaseControl control = new GetProductDetailUseCaseControl(
			outputBoundary, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.hasError);
	}
	
	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<com.motorbike.domain.entities.SanPham> findById(Long id) {
			if (id == null || id <= 0 || id == 999L || id == Long.MAX_VALUE) {
				return Optional.empty();
			}
			
			int stock = 100;
			if (id == 100L) {
				stock = 0;
			} else if (id == 200L) {
				stock = 1000;
			} else if (id == 300L) {
				stock = 5;
			}
			
			XeMay product = new XeMay(
				"Honda Wave Alpha",
				"Xe số tiết kiệm nhiên liệu",
				new BigDecimal("30000000"),
				"honda-wave.jpg",
				stock,
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
			return productId != null && productId > 0 && productId != 999L && productId != Long.MAX_VALUE;
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.SanPham> findAll() {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public void deleteById(Long productId) {
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.PhuKienXeMay> findAllAccessories() {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.PhuKienXeMay> searchAccessories(String keyword) {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> findAllMotorbikes() {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> searchMotorbikes(String keyword) {
			return new java.util.ArrayList<>();
		}
	}
}
