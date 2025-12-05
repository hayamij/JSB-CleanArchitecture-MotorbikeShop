package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.SearchMotorbikesPresenter;
import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;
import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.domain.entities.XeMay;

public class SearchMotorbikesUseCaseControlTest {

	@Test
	public void testExecute_WithKeyword_Success() {
		SearchMotorbikesInputData inputData = new SearchMotorbikesInputData("Honda", null, null, null, null, null);
		
		MotorbikeRepository productRepo = new MockMotorbikeRepository();
		SearchMotorbikesViewModel viewModel = new SearchMotorbikesViewModel();
		SearchMotorbikesOutputBoundary outputBoundary = new SearchMotorbikesPresenter(viewModel);
		
		SearchMotorbikesUseCaseControl control = new SearchMotorbikesUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.motorbikes);
		assertEquals(1, viewModel.motorbikes.size());
	}

	@Test
	public void testExecute_NoResults() {
		SearchMotorbikesInputData inputData = new SearchMotorbikesInputData("xyz123", null, null, null, null, null);
		
		MotorbikeRepository productRepo = new MockMotorbikeRepository();
		SearchMotorbikesViewModel viewModel = new SearchMotorbikesViewModel();
		SearchMotorbikesOutputBoundary outputBoundary = new SearchMotorbikesPresenter(viewModel);
		
		SearchMotorbikesUseCaseControl control = new SearchMotorbikesUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.motorbikes);
		assertEquals(0, viewModel.motorbikes.size());
	}

	@Test
	public void testExecute_NullInputData() {
		MotorbikeRepository productRepo = new MockMotorbikeRepository();
		SearchMotorbikesViewModel viewModel = new SearchMotorbikesViewModel();
		SearchMotorbikesOutputBoundary outputBoundary = new SearchMotorbikesPresenter(viewModel);
		
		SearchMotorbikesUseCaseControl control = new SearchMotorbikesUseCaseControl(outputBoundary, productRepo);
		control.execute(null);
		
		// Use case catches NullPointerException and returns system error
		assertTrue(viewModel.hasError);
		assertNotNull(viewModel.errorMessage);
	}

	private static class MockMotorbikeRepository implements MotorbikeRepository {
		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			List<XeMay> motorbikes = new ArrayList<>();
			if (keyword != null && keyword.toLowerCase().contains("honda")) {
				motorbikes.add(new XeMay("Honda Wave", "Xe số tiết kiệm", new BigDecimal("20000000"), "wave.jpg", 10, "Honda", "Wave", "Đỏ", 2024, 110));
			}
			return motorbikes;
		}

		@Override
		public Optional<XeMay> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public XeMay save(XeMay xeMay) {
			return xeMay;
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
			List<XeMay> motorbikes = new ArrayList<>();
			motorbikes.add(new XeMay("Honda Wave", "Xe số tiết kiệm", new BigDecimal("20000000"), "wave.jpg", 10, "Honda", "Wave", "Đỏ", 2024, 110));
			motorbikes.add(new XeMay("Yamaha Exciter", "Xe thể thao", new BigDecimal("50000000"), "exciter.jpg", 5, "Yamaha", "Exciter", "Xanh", 2024, 155));
			return motorbikes;
		}
	}
}
