package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.DeleteAccessoryPresenter;
import com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel;
import com.motorbike.business.dto.accessory.DeleteAccessoryInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

public class DeleteAccessoryUseCaseControlTest {

	@Test
	public void testExecute_ValidDelete_Success() {
		DeleteAccessoryInputData inputData = new DeleteAccessoryInputData(1L);
		
		ProductRepository productRepo = new MockProductRepository();
		DeleteAccessoryViewModel viewModel = new DeleteAccessoryViewModel();
		DeleteAccessoryOutputBoundary outputBoundary = new DeleteAccessoryPresenter(viewModel);
		
		DeleteAccessoryUseCaseControl control = new DeleteAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
	}

	@Test
	public void testExecute_ProductNotFound() {
		DeleteAccessoryInputData inputData = new DeleteAccessoryInputData(999L);
		
		ProductRepository productRepo = new MockProductRepository();
		DeleteAccessoryViewModel viewModel = new DeleteAccessoryViewModel();
		DeleteAccessoryOutputBoundary outputBoundary = new DeleteAccessoryPresenter(viewModel);
		
		DeleteAccessoryUseCaseControl control = new DeleteAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	@Test
	public void testExecute_NullInputData() {
		ProductRepository productRepo = new MockProductRepository();
		DeleteAccessoryViewModel viewModel = new DeleteAccessoryViewModel();
		DeleteAccessoryOutputBoundary outputBoundary = new DeleteAccessoryPresenter(viewModel);
		
		DeleteAccessoryUseCaseControl control = new DeleteAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id == 1L) {
				// Return a test accessory product
				return Optional.of(new PhuKienXeMay(
					1L, "Test Accessory", "Test Description",
					java.math.BigDecimal.valueOf(100000), "test.jpg", 10, true,
					null, null, "Test Type", "Test Brand", "Test Material", "Test Size"
				));
			}
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return productId == 1L;
		}

		@Override
		public java.util.List<SanPham> findAll() {
			return new java.util.ArrayList<>();
		}

		@Override
		public void deleteById(Long productId) {
		}

		@Override
		public java.util.List<PhuKienXeMay> findAllAccessories() {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<PhuKienXeMay> searchAccessories(String keyword) {
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
		
		@Override
		public Optional<SanPham> findByTenSanPham(String tenSanPham) {
			return Optional.empty();
		}
		
		@Override
		public Optional<SanPham> findByMaSanPham(String maSanPham) {
			return Optional.empty();
		}
	}
}
