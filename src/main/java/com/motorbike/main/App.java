package com.motorbike.main;

import com.motorbike.main.business.usecase.ListOrdersControl;
import com.motorbike.main.business.usecase.ListOrdersInputBoundary;
import com.motorbike.main.persistence.MockOrderGateway;
import com.motorbike.main.persistence.OrderGateway;
import com.motorbike.main.presentation.OrderListController;
import com.motorbike.main.presentation.OrderListPresenter;
import com.motorbike.main.presentation.OrderListViewModel;
import com.motorbike.main.presentation.OrderListWindow;

public class App {
    public static void main(String[] args) {
        // === Wiring các tầng ===
        OrderGateway gateway = new MockOrderGateway();
        OrderListViewModel viewModel = new OrderListViewModel();
        OrderListPresenter presenter = new OrderListPresenter(viewModel);
        ListOrdersInputBoundary useCase = new ListOrdersControl(gateway, presenter);
        OrderListController controller = new OrderListController(useCase);
        OrderListWindow ui = new OrderListWindow(controller, viewModel);
        ui.initialize();

        // === Gọi use case: Xem danh sách đơn hàng ===
        ui.xemDanhSach();
    }
}

