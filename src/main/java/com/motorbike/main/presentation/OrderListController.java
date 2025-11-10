package com.motorbike.main.presentation;


import com.motorbike.main.business.usecase.ListOrdersInputBoundary;
import com.motorbike.main.shared.ListOrdersRequestData;

public class OrderListController {
    private final ListOrdersInputBoundary input;

    public OrderListController(ListOrdersInputBoundary input) {
        this.input = input;
    }

    public void execute() {
        input.execute(new ListOrdersRequestData());
    }

}

