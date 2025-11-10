package com.motorbike.main.presentation;

import com.motorbike.main.business.usecase.ListOrdersOutputBoundary;
import com.motorbike.main.shared.ListOrdersResponseData;

public class OrderListPresenter implements ListOrdersOutputBoundary {

    private final OrderListViewModel viewModel;

    public OrderListPresenter(OrderListViewModel vm) {
        this.viewModel = vm;
    }

    @Override
    public void present(ListOrdersResponseData response) {
        // Dữ liệu đã là OrderDTO, chỉ cần cập nhật ViewModel
        viewModel.setOrders(response.getOrders());
    }
}
