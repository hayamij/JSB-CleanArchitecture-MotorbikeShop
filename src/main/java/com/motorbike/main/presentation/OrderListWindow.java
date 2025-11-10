package com.motorbike.main.presentation;

import com.motorbike.main.shared.Subscriber;

public class OrderListWindow implements Subscriber {
    private final OrderListController controller;
    private final OrderListViewModel viewModel;

    public OrderListWindow(OrderListController controller, OrderListViewModel vm) {
        this.controller = controller;
        this.viewModel = vm;
    }
    public void initialize() {
        this.viewModel.addSubscriber(this);
    }

    public void xemDanhSach() {
        controller.execute();
    }

    @Override
    public void update() {
        System.out.println("=== DANH SÁCH ĐƠN HÀNG ===");
        viewModel.getOrders().forEach(System.out::println);
        System.out.println("===========================");
    }
}
