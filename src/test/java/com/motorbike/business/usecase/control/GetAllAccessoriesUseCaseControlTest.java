package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetAllAccessoriesPresenter;
import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

public class GetAllAccessoriesUseCaseControlTest {

	@Test
	public void testExecute_WithAccessories_Success() {
		ProductRepository productRepo = new MockProductRepository();
		GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
		GetAllAccessoriesOutputBoundary outputBoundary = new GetAllAccessoriesPresenter(viewModel);
		
		GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertFalse(viewModel.hasError);
		assertEquals(2, viewModel.accessories.size());
	}

	@Test
	public void testExecute_EmptyList_Success() {
		ProductRepository productRepo = new EmptyProductRepository();
		GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
		GetAllAccessoriesOutputBoundary outputBoundary = new GetAllAccessoriesPresenter(viewModel);
		
		GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertFalse(viewModel.hasError);
		assertEquals(0, viewModel.accessories.size());
	}

	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return false;
		}

		@Override
		public List<SanPham> findAll() {
			return new ArrayList<>();
		}

		@Override
		public void deleteById(Long productId) {
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			List<PhuKienXeMay> accessories = new ArrayList<>();
			accessories.add(new PhuKienXeMay(
				1L, "Mũ bảo hiểm", "Mũ Fullface", new BigDecimal("500000"), 
				"helmet.jpg", 100, true, LocalDateTime.now(), LocalDateTime.now(),
				"Mũ bảo hiểm", "Royal", "ABS", "L"
			));
			accessories.add(new PhuKienXeMay(
				2L, "Găng tay", "Găng tay da", new BigDecimal("200000"), 
				"glove.jpg", 50, true, LocalDateTime.now(), LocalDateTime.now(),
				"Găng tay", "Alpinestars", "Da", "M"
			));
			return accessories;
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> findAllMotorbikes() {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> searchMotorbikes(String keyword) {
			return new ArrayList<>();
		}
	}

	private static class EmptyProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return false;
		}

		@Override
		public List<SanPham> findAll() {
			return new ArrayList<>();
		}

		@Override
		public void deleteById(Long productId) {
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return new ArrayList<>();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> findAllMotorbikes() {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> searchMotorbikes(String keyword) {
			return new ArrayList<>();
		}
	}
}
