package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetAllMotorbikesPresenter;
import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;

public class GetAllMotorbikesUseCaseControlTest {

	@Test
	public void testExecute_WithMotorbikes_Success() {
		ProductRepository productRepo = new MockProductRepositoryWithData();
		GetAllMotorbikesViewModel viewModel = new GetAllMotorbikesViewModel();
		GetAllMotorbikesOutputBoundary outputBoundary = new GetAllMotorbikesPresenter(viewModel);
		
		GetAllMotorbikesUseCaseControl control = new GetAllMotorbikesUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.motorbikes);
		assertEquals(2, viewModel.motorbikes.size());
	}

	@Test
	public void testExecute_EmptyList() {
		ProductRepository productRepo = new MockProductRepositoryEmpty();
		GetAllMotorbikesViewModel viewModel = new GetAllMotorbikesViewModel();
		GetAllMotorbikesOutputBoundary outputBoundary = new GetAllMotorbikesPresenter(viewModel);
		
		GetAllMotorbikesUseCaseControl control = new GetAllMotorbikesUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.motorbikes);
		assertEquals(0, viewModel.motorbikes.size());
	}

	private static class MockProductRepositoryWithData implements ProductRepository {
		@Override
		public List<XeMay> findAllMotorbikes() {
			List<XeMay> motorbikes = new ArrayList<>();
			motorbikes.add(new XeMay("Honda Wave", "Xe số tiết kiệm", new BigDecimal("20000000"), "wave.jpg", 10, "Honda", "Wave", "Đỏ", 2024, 110));
			motorbikes.add(new XeMay("Yamaha Exciter", "Xe thể thao", new BigDecimal("50000000"), "exciter.jpg", 5, "Yamaha", "Exciter", "Xanh", 2024, 155));
			return motorbikes;
		}

		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public boolean existsById(Long id) {
			return false;
		}

		@Override
		public List<SanPham> findAll() {
			List<SanPham> allProducts = new ArrayList<>();
			allProducts.add(new XeMay("Honda Wave", "Xe số tiết kiệm", new BigDecimal("20000000"), "wave.jpg", 10, "Honda", "Wave", "Đỏ", 2024, 110));
			allProducts.add(new XeMay("Yamaha Exciter", "Xe thể thao", new BigDecimal("50000000"), "exciter.jpg", 5, "Yamaha", "Exciter", "Xanh", 2024, 155));
			return allProducts;
		}

		public List<com.motorbike.domain.entities.PhuKienXeMay> findAllAccessories() {
			return new ArrayList<>();
		}

		public List<XeMay> searchMotorbikes(String keyword) {
			return new ArrayList<>();
		}

		public List<com.motorbike.domain.entities.PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}
	}

	private static class MockProductRepositoryEmpty implements ProductRepository {
		@Override
		public List<XeMay> findAllMotorbikes() {
			return new ArrayList<>();
		}

		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public boolean existsById(Long id) {
			return false;
		}

		@Override
		public List<SanPham> findAll() {
			return new ArrayList<>();
		}

		@Override
		public List<com.motorbike.domain.entities.PhuKienXeMay> findAllAccessories() {
			return new ArrayList<>();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return new ArrayList<>();
		}

		@Override
		public List<com.motorbike.domain.entities.PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}
	}
}
