package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetAllProductsPresenter;
import com.motorbike.adapters.viewmodels.GetAllProductsViewModel;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetAllProductsOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;

public class GetAllProductsUseCaseControlTest {

	@Test
	public void testExecute_WithMixedProducts_Success() {
		ProductRepository productRepo = new MockProductRepositoryWithMixedProducts();
		GetAllProductsViewModel viewModel = new GetAllProductsViewModel();
		GetAllProductsOutputBoundary outputBoundary = new GetAllProductsPresenter(viewModel);
		
		GetAllProductsUseCaseControl control = new GetAllProductsUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertTrue(viewModel.success);
		assertNotNull(viewModel.products);
		assertEquals(4, viewModel.products.size());
	}

	@Test
	public void testExecute_EmptyList() {
		ProductRepository productRepo = new MockProductRepositoryEmpty();
		GetAllProductsViewModel viewModel = new GetAllProductsViewModel();
		GetAllProductsOutputBoundary outputBoundary = new GetAllProductsPresenter(viewModel);
		
		GetAllProductsUseCaseControl control = new GetAllProductsUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertTrue(viewModel.success);
		assertNotNull(viewModel.products);
		assertEquals(0, viewModel.products.size());
	}

	// Mock repositories
	private static class MockProductRepositoryWithMixedProducts implements ProductRepository {
		@Override
		public List<SanPham> findAll() {
			List<SanPham> products = new ArrayList<>();
			products.add(new XeMay("Honda Wave", "Xe số tiết kiệm", new BigDecimal("20000000"), "wave.jpg", 10, "Honda", "Wave", "Đỏ", 2024, 110));
			products.add(new XeMay("Yamaha Exciter", "Xe thể thao", new BigDecimal("50000000"), "exciter.jpg", 5, "Yamaha", "Exciter", "Xanh", 2024, 155));
			products.add(new PhuKienXeMay("Mũ bảo hiểm", "Mũ fullface", new BigDecimal("500000"), "helmet.jpg", 20, "Nón", "Royal", "Plastic", "L"));
			products.add(new PhuKienXeMay("Găng tay", "Găng tay da", new BigDecimal("200000"), "gloves.jpg", 15, "Găng", "Dainese", "Da", "M"));
			return products;
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
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
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

	private static class MockProductRepositoryEmpty implements ProductRepository {
		@Override
		public List<SanPham> findAll() {
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
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
		}

		@Override
		public Optional<SanPham> findByTenSanPham(String tenSanPham) {
			return Optional.empty();
		}

		@Override
		public Optional<SanPham> findByMaSanPham(String maSanPham) {
			return Optional.empty();
		}	}
}