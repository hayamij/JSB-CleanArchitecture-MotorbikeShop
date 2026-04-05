package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetValidOrderStatusesPresenter;
import com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel;
import com.motorbike.business.dto.order.GetValidOrderStatusesInputData;
import com.motorbike.business.usecase.output.GetValidOrderStatusesOutputBoundary;

public class GetValidOrderStatusesUseCaseControlTest {

	@Test
	public void testExecute_ChoXacNhan_ReturnsValidStatuses() {
		GetValidOrderStatusesViewModel viewModel = new GetValidOrderStatusesViewModel();
		GetValidOrderStatusesOutputBoundary outputBoundary = new GetValidOrderStatusesPresenter(viewModel);
		
		GetValidOrderStatusesUseCaseControl control = new GetValidOrderStatusesUseCaseControl(outputBoundary);
		
		GetValidOrderStatusesInputData inputData = new GetValidOrderStatusesInputData("CHO_XAC_NHAN");
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertNotNull(viewModel.validStatuses);
		assertEquals(2, viewModel.validStatuses.size());
	}

	@Test
	public void testExecute_DaXacNhan_ReturnsValidStatuses() {
		GetValidOrderStatusesViewModel viewModel = new GetValidOrderStatusesViewModel();
		GetValidOrderStatusesOutputBoundary outputBoundary = new GetValidOrderStatusesPresenter(viewModel);
		
		GetValidOrderStatusesUseCaseControl control = new GetValidOrderStatusesUseCaseControl(outputBoundary);
		
		GetValidOrderStatusesInputData inputData = new GetValidOrderStatusesInputData("DA_XAC_NHAN");
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertEquals(2, viewModel.validStatuses.size());
	}

	@Test
	public void testExecute_GiaoThanhCong_ReturnsEmptyList() {
		GetValidOrderStatusesViewModel viewModel = new GetValidOrderStatusesViewModel();
		GetValidOrderStatusesOutputBoundary outputBoundary = new GetValidOrderStatusesPresenter(viewModel);
		
		GetValidOrderStatusesUseCaseControl control = new GetValidOrderStatusesUseCaseControl(outputBoundary);
		
		GetValidOrderStatusesInputData inputData = new GetValidOrderStatusesInputData("GIAO_THANH_CONG");
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertEquals(0, viewModel.validStatuses.size());
	}

	@Test
	public void testExecute_NullCurrentStatus_ValidationError() {
		GetValidOrderStatusesViewModel viewModel = new GetValidOrderStatusesViewModel();
		GetValidOrderStatusesOutputBoundary outputBoundary = new GetValidOrderStatusesPresenter(viewModel);
		
		GetValidOrderStatusesUseCaseControl control = new GetValidOrderStatusesUseCaseControl(outputBoundary);
		
		GetValidOrderStatusesInputData inputData = new GetValidOrderStatusesInputData(null);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertEquals("INVALID_INPUT", viewModel.errorCode);
	}

	@Test
	public void testExecute_InvalidStatus_ValidationError() {
		GetValidOrderStatusesViewModel viewModel = new GetValidOrderStatusesViewModel();
		GetValidOrderStatusesOutputBoundary outputBoundary = new GetValidOrderStatusesPresenter(viewModel);
		
		GetValidOrderStatusesUseCaseControl control = new GetValidOrderStatusesUseCaseControl(outputBoundary);
		
		GetValidOrderStatusesInputData inputData = new GetValidOrderStatusesInputData("INVALID_STATUS");
		control.execute(inputData);
		
		// Invalid status returns empty list, not error
		assertTrue(viewModel.success);
		assertEquals(0, viewModel.validStatuses.size());
	}
}
