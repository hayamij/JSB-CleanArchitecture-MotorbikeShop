package com.motorbike.main.business.usecase;


import com.motorbike.main.persistence.OrderGateway;
import com.motorbike.main.shared.ListOrdersRequestData;
import com.motorbike.main.shared.ListOrdersResponseData;

public class ListOrdersControl extends OrderControl implements ListOrdersInputBoundary {

    private final OrderGateway gateway;
    private final ListOrdersOutputBoundary output;

    public ListOrdersControl(OrderGateway gateway, ListOrdersOutputBoundary output) {
        this.gateway = gateway;
        this.output = output;
    }

    @Override
    public void execute(ListOrdersRequestData request) {
        var orders = gateway.findAll();
        var response = new ListOrdersResponseData(orders);
        output.present(response);
    }

    @Override
    public void execute() {
        execute(new ListOrdersRequestData());
    }
}

